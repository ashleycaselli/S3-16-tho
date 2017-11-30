//--------------------------GLOBAL-VARIABLES--------------------------------
var lastClueText = "No clue available";     //active clue
var poiToReach = "";                        //name of the next poi
var longitudeToReach = "";                  //longitude of the next poi
var latitudeToReach = "";                   //latitude of the next poi
var loggedTeam = "";                        //name of logged team
var currentHunt = "";                       //current hunt ID
var currentHuntName = "";                   //current hunt name
var currentQuiz = "";
var currentQuizAnswer = "";
var googleScriptLoaded = false;
var poiToBeSent = "";
var poiFound = 0;
var thID = 0;
var loggedTeamID = 0;                       //ID of the logged Team
var clueID = 0;                             //ID of the clue saved in memory

//----------------------------EVENT-LISTENERS--------------------------------
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    //load saved data
    loggedTeam = localStorage.getItem('loggedTeam');
    if (loggedTeam == null) loggedTeam = "";
    currentHunt = localStorage.getItem('currentHunt');
    if (currentHunt == null) currentHunt = "";
    currentHuntName = localStorage.getItem('currentHuntName');
    if (currentHuntName == null) currentHuntName = "";
    poiToBeSent = localStorage.getItem('poiToBeSent');
    if (poiToBeSent == null) poiToBeSent = "";
    poiToReach = localStorage.getItem('poiToReach');
    if (poiToReach == null) poiToReach = "";
    longitudeToReach = localStorage.getItem('poiToBeSent');
    if (poiToBeSent == null) poiToBeSent = "";
    latitudeToReach = localStorage.getItem('latitudeToReach');
    if (latitudeToReach == null) latitudeToReach = "";
    currentQuiz = localStorage.getItem('currentQuiz');
    if (currentQuiz == null) currentQuiz = "";
    currentQuizAnswer = localStorage.getItem('currentQuizAnswer');
    if (currentQuizAnswer == null) currentQuizAnswer = "";
    lastClueText = localStorage.getItem('lastClueText');
    if (poiToBeSent == null) poiToBeSent = "";
    poiFound = localStorage.getItem('poiFound');
    if (poiFound == null) poiFound = 0;
    thID = localStorage.getItem('thID');
    if (thID == null) thID = 0;
    loggedTeamID = localStorage.getItem('loggedTeamID');
    if (loggedTeamID == null) loggedTeamID = 0;
    clueID = localStorage.getItem('clueID');
    if (clueID == null) clueID = 0;
    connect();
}

//-------------------------connection-variables------------------------------
var mq_username = "test";
var mq_password = "test";
var mq_vhost = "/";
var mq_url = 'http://52.14.140.101:15674/stomp';
//var mq_url = 'http://localhost:15674/stomp';
var mq_queue = "/exchange/organizer-message";
var ws;
var client;
var firstConnection = true;

//--------------------------server-comunication------------------------------
function connect() {
    Stomp.WebSocketClass = SockJS;
    ws = new SockJS(mq_url);
    client = Stomp.over(ws);
    client.heartbeat.outgoing = 0;
    client.heartbeat.incoming = 0;
    client.onreceive = onReceive;
    client.connect(mq_username, mq_password, on_connect, on_connect_error, mq_vhost);
}

function on_connect() {
    client.subscribe('/topic/win', function (d) {
        var p = JSON.parse(d.body);
        var th = p.th;
        var team = p.team;
        if (th == thID) {
            if (team == loggedTeam) {
                alert("hai vinto");
            } else {
                alert("Qualcuno ha vinto. Non tu.");
            }
        }
    });
    console.log("connected to server");
    if (firstConnection == true) {
        //check if logged team exists
        if (loggedTeam != "") {
            showLoggedTeamName();
            if (isThRunning()) {
                showCurrentTreasureHuntPage();
            } else {
                requireTHlist();
            }
        } else {
            showNotLoggedUser();
        }
        firstConnection = false;
    }
}

function on_message(m) {
}

function on_connect_error() {
    console.log("connection error");
    //connect();
    alert("no connection");
}

var onReceive = function (m) {
    var msg = JSON.parse(m.body);
    var messageType = msg.messageType;
    var payload = JSON.parse(msg.payload);
    var sender = JSON.parse(msg.sender);
    console.log(payload);
    if (messageType == "LoginMsg") {
        loginResult(payload);
    }
    if (messageType == "RegistrationMsg") {
        registrationResult(payload);
    }
    if (messageType == "ListTHsMsg") {
        showNearTreasureHunt(payload.list);
    }
    if (messageType == "PoiMsg") {
        handlePoiMsg(m, payload, sender);
    }
}

function handlePoiMsg(m, payload, sender) {
    //var poiID = payload.ID;
    var poiName = payload.name;
    var treasureHuntID = payload.treasureHuntID;
    var position = JSON.parse(payload.position);
    var latitude = position.latitude;
    var longitude = position.longitude;
    var quiz = JSON.parse(payload.quiz);
    var question = quiz.question;
    var answer = quiz.answer;
    var clue = JSON.parse(payload.clue);
    var clueText = clue.content;

    if (thID == 0) {
        thID = treasureHuntID;
        localStorage.setItem('thID', thID);
    }
    if (loggedTeamID == 0) {
        loggedTeamID = sender;
        localStorage.setItem('loggedTeamID', loggedTeamID);
    }
    poiToBeSent = m.body;
    localStorage.setItem('poiToBeSent', poiToBeSent);
    lastClueText = clueText;
    localStorage.setItem('lastClueText', lastClueText);
    currentQuiz = question;
    localStorage.setItem('currentQuiz', currentQuiz);
    currentQuizAnswer = answer;
    localStorage.setItem('currentQuizAnswer', currentQuizAnswer);
    latitudeToReach = latitude;
    localStorage.setItem('latitudeToReach', latitudeToReach);
    longitudeToReach = longitude;
    localStorage.setItem('longitudeToReach', longitudeToReach);
    if (poiToReach == "") {
        alert("Try to find the first point of interest named: " + poiName + ".\nThe clue will help you.");
    } else {
        alert("Poi: " + poiToReach + " found.\nNext Poi is: " + poiName + ".\nThe clue will help you.");
    }
    poiToReach = poiName;
    localStorage.setItem('poiToReach', poiToReach);
    clueID = clue.ID;
    localStorage.setItem('clueID', clueID);
}

function send(m) {
    client.send(mq_queue, {'reply-to': '/temp-queue/foo', "content-type": "text/plain"}, m);
}

function requireTHlist() {
    send('{"messageType":"ListTHsMsg","sender":"2","payload":"{\\"list\\":[]}"}');
}

//-----------------------------select-th-----------------------------------
function showSelectTreasureHuntContainer() {     // Shows the container and hides all the contents
    document.getElementById("selectTreasureHuntPage").style.display = "block";
    document.getElementById("codeButtons").style.display = "none";
    document.getElementById("insertCodeManually").style.display = "none";
    document.getElementById("currentTreasureHunt").style.display = "none";
    document.getElementById("nearTreasureHunt").innerHTML = "";
    document.getElementById("nearTreasureHunt").style.display = "none";
    document.getElementById("notLoggedUserPage").style.display = "none";
}

function showCurrentTreasureHuntPage() {        // Shows leave/resume buttons in SelectTreasureHuntContainer
    showSelectTreasureHuntContainer();          // empty container
    document.getElementById("treasureHuntName").innerHTML = currentHuntName;
    document.getElementById("currentTreasureHunt").style.display = "block";
}

function showNearTreasureHunt(list) {           // Shows list of available THs
    showSelectTreasureHuntContainer();          // empty container
    var div = document.getElementById("nearTreasureHunt");
    var content = "";
    if (list.length > 0) {
        content += '<p class="available-hunts-label">Available treasure hunts</p><ul>';
        for (var i = 0; i < list.length; i++) {
            content += '<li onclick=\'showScanButtons("' + list[i].ID + '","' + list[i].name + '")\' >' + list[i].name + '</li>';
        }
        content += "</ul>";
    } else {
        content += "<p class='available-hunts-label'>No available treasure hunt.<br>Try again later.</p>" +
            "<div class='refresh-button'><input type='button' value='refresh' onclick='requireTHlist()'></div>";
    }
    div.innerHTML = content;
    div.style.display = "block";
}

function showScanButtons(selectedTH, selectedTHname) { // Shows scan/type buttons in SelectTreasureHuntContainer
    showSelectTreasureHuntContainer();          // empty container
    document.getElementById("scanQR").onclick = function () {
        scanQRCode("" + selectedTH, selectedTHname)
    };
    document.getElementById("writeCode").onclick = function () {
        writeQRCode("" + selectedTH, selectedTHname)
    };
    document.getElementById("codeButtonsLabel").innerHTML = selectedTHname;
    document.getElementById("codeButtons").style.display = "block";
}

function scanQRCode(selectedTH, selectedTHname) {
    cordova.plugins.barcodeScanner.scan(
        function (result) {
            checkScanResults(selectedTH, result.text, selectedTHname);
        },
        function (error) {
            alert("Scanning failed: " + error);
        }
    );
}

function writeQRCode(selectedTH, selectedTHname) {
    showSelectTreasureHuntContainer();          // empty container
    document.getElementById("saveCodeButton").onclick = function () {
        checkScanResults(selectedTH, document.getElementById("insertCodeInput").value, selectedTHname);
    }
    document.getElementById("cancelCodeButton").onclick = function () {
        showSelectTreasureHuntContainer();          // empty container
        document.getElementById("codeButtons").style.display = "block";
    }
    document.getElementById("insertCodeInput").value = "";
    document.getElementById("insertCodeManually").style.display = "block";
    document.getElementById("insertCodeInput").focus()
}

function checkScanResults(selectedTH, result, selectedTHname) {
    if (result == selectedTH) {
        currentHunt = selectedTH;
        currentHuntName = selectedTHname;
        localStorage.setItem("currentHunt", selectedTH);
        localStorage.setItem("currentHuntName", selectedTHname);
        document.getElementById("selectTreasureHuntPage").style.display = "none";
        document.getElementById("insertCodeManually").style.display = "none";
        loadMapPage();
        send('{"messageType":"PoiMsg","sender":"0","payload":"{\\"ID\\":0,\\"name\\":\\"' + loggedTeam + '\\",\\"treasureHuntID\\":' + currentHunt + ',\\"position\\":\\"{\\\\\\"latitude\\\\\\":0,\\\\\\"longitude\\\\\\":0}\\",\\"quiz\\":\\"{\\\\\\"ID\\\\\\":0,\\\\\\"question\\\\\\":\\\\\\"\\\\\\",\\\\\\"answer\\\\\\":\\\\\\"\\\\\\"}\\",\\"clue\\":\\"{\\\\\\"ID\\\\\\":0,\\\\\\"content\\\\\\":\\\\\\"\\\\\\"}\\"}"}');
    } else {
        alert("invalid code");
    }
}

function resumeTreasureHunt() {
    addGoogleMapsScript();
    showSelectTreasureHuntContainer();
    document.getElementById("selectTreasureHuntPage").style.display = "none";
    document.getElementById("mapPage").style.display = "block";
    document.getElementById("mapPageTitle").innerText = currentHuntName;
}

function leaveTreasureHunt() {
    unsubscribeTreasureHunt();
    //clean variables
    currentHunt = "";
    currentHuntName = "";
    poiToReach = "";
    poiToBeSent = "";
    latitudeToReach = "";
    currentQuiz = "";
    currentQuizAnswer = "";
    thID = 0;
    //clean saved data
    localStorage.removeItem('thID');
    localStorage.removeItem('currentHunt');
    localStorage.removeItem('currentHuntName');
    localStorage.removeItem('poiToReach');
    localStorage.removeItem('poiToBeSent');
    localStorage.removeItem('latitudeToReach');
    localStorage.removeItem('currentQuiz');
    localStorage.removeItem('currentQuizAnswer');
    //go to available treasure hunt list
    showSelectTreasureHuntContainer();
    requireTHlist();
}

//-------------------------------MAP----------------------------------------
function loadMapPage() {
    document.getElementById("mapPage").style.display = "block";
    document.getElementById("googleMap").style.display = "block";
    document.getElementById("mapPageTitle").innerText = currentHuntName;
    addGoogleMapsScript();
}

function addGoogleMapsScript() {
    if (!googleScriptLoaded) {
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBNQr4YcrvttSMIgWOX68kJnigaI0Cir9c&callback=mapLoadedCallback';
        document.head.appendChild(script);
        googleScriptLoaded = true;
    }
}

function mapLoadedCallback() {
    getLocation(function (firstLatitude, firstLongitude) {
        var mapProp = {
            center: new google.maps.LatLng(firstLatitude, firstLongitude),
            zoom: 15,
        };
        var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
        var marker = new google.maps.Marker({
            position: {lat: firstLatitude, lng: firstLongitude},
            map: map,
            title: "player-position"
        });
        setInterval(function () {
            getLocation(function (latitude, longitude) {
                //map.setCenter(new google.maps.LatLng(latitude, longitude));
                marker.setPosition({lat: latitude, lng: longitude});
                var error = 0.0002;
                if (latitudeToReach != "" && longitudeToReach != "") {
                    if (latitude * 1 >= latitudeToReach * 1 - error * 1 && latitude * 1 <= latitudeToReach * 1 + error * 1) {
                        if (longitude * 1 >= longitudeToReach * 1 - error * 1 && longitude * 1 <= longitudeToReach * 1 + error * 1) {
                            if (currentQuiz == "") {
                                send(poiToBeSent);
                                poiFound++;
                                localStorage.setItem('poiFound', poiFound);
                            }
                        }
                    }
                }
            })
        }, 3000);
    })
}

function getLocation(callback) {
    function onSuccess(position) {
        callback(position.coords.latitude, position.coords.longitude)
    }

    function onError(error) {
        alert('Code: ' + error.code + '\nMessage: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(onSuccess, onError);
}

function exitFromMap() {
    document.getElementById("mapPage").style.display = "none";
    document.getElementById("selectTreasureHuntPage").style.display = "block";
    document.getElementById("codeButtons").style.display = "none";
    document.getElementById("treasureHuntName").innerHTML = currentHuntName;
    document.getElementById("currentTreasureHunt").style.display = "block";
}

//-------------------------------CLUE----------------------------------------

function showClue() {
    if (currentQuiz == "") {
        var container = document.getElementById("mapPageClue");
        var content = '<h1>CLUE</h1>';
        content += '<p>' + lastClueText + '</p>';
        content += '<div onClick="closeClue()" class="clue-quiz-button"><span>HIDE</span></div>';
        container.innerHTML = content;
        container.style.display = "block";
    } else {
        showQuizAlert();
    }
}

function closeClue() {
    document.getElementById("mapPageClue").style.display = "none";
}

//---------------------------------QUIZ------------------------------------

function showQuizAlert() {
    var container = document.getElementById("mapPageQuiz");
    var content = '<h1>NOT YET</h1>';
    content += '<p>Clues are precious, you must solve a quiz before!</p>';
    content += '<div onClick="showQuiz();" class="clue-quiz-button"><span>OK</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function showQuiz() {
    var container = document.getElementById("mapPageQuiz");
    var content = '<h1>QUIZ</h1>';
    content += '<p>' + currentQuiz + '</p>';
    content += '<input class="quiz-input" id="quizInput" type="text" />'
    content += '<div onClick="checkQuiz();" class="clue-quiz-button"><span>CHECK</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function checkQuiz() {
    if (document.getElementById("quizInput").value.trim().toUpperCase() === currentQuizAnswer.toUpperCase()) {
        document.getElementById("mapPageQuiz").style.display = "none";
        //registro l'evento sul DB
        send('{"messageType":"ClueMsg","sender":"' + loggedTeamID + '","payload":"' + clueID + '-' + thID + '"}');
        alert("clue unlocked");
        currentQuiz = "";
        showClue();
    } else {
        alert("wrong answer");
    }
}

//---------------------------------PROGRESS---------------------------------
function viewProgress() {
    var div = document.getElementById("mapPageProgress");
    var content = "";
    content += '<div class="close-progress" onclick="closeProgress()">X</div><div class="out-of-progress">' + poiFound + '</br>POI</br>reached</div>';
    div.innerHTML = content;
    div.style.display = "block";
}

function closeProgress() {
    document.getElementById("mapPageProgress").style.display = "none";
}

//---------------------------------LOGIN---------------------------------
function showLoggedTeamName() {             // shows bottom bar containing team name
    userInfo = document.getElementById("userInfo");
    userInfo.innerHTML = '<span onclick="logoutTeam();">Team: ' + loggedTeam + '</span>';
    userInfo.style.display = "block";
}

function showNotLoggedUser() {              // shows choice login/create-team
    document.getElementById("notLoggedUserPage").style.display = "block";
    document.getElementById("createOrLogin").style.display = "block";
    document.getElementById("loginTeam").style.display = "none";
    document.getElementById("createTeam").style.display = "none";
}

function showCreateTeamPage() {             // shows create-team form
    document.getElementById("createOrLogin").style.display = "none";
    document.getElementById("createTeam").style.display = "block";
    document.getElementById("createTeamNameInput").focus();
}

function createTeam() {                     //creates the team
    var username = document.getElementById("createTeamNameInput").value;
    var password = document.getElementById("createTeamPassInput").value;
    if (password === document.getElementById("createTeamConfirmPassInput").value) {
        send('{"messageType":"RegistrationMsg","sender":"0","payload":"{\\"username\\":\\"' + username + '\\",\\"password\\":\\"' + password + '\\"}"}');
    } else {
        alert("Password Mismatch");
    }
}

function registrationResult(value) {
    if (value == "200") {
        loggedTeam = document.getElementById("createTeamNameInput").value;
        localStorage.setItem('loggedTeam', loggedTeam);
        showLoggedTeamName();
        showSelectTreasureHuntContainer();
        //check if a TH is running
        if (isThRunning()) {
            showCurrentTreasureHuntPage();
        } else {
            requireTHlist();
        }
    } else {
        showNotLoggedUser();
        alert("This name is no longer available");
    }
}

function isThRunning() {
    if (currentHunt != "" && currentHunt != null) {
        return true;
    }
    return false;
}

function showLoginTeamPage() {
    document.getElementById("createOrLogin").style.display = "none";
    document.getElementById("loginTeam").style.display = "block";
    document.getElementById("loginTeamNameInput").focus();
}

function loginTeam() {
    document.getElementById("notLoggedUserPage").style.display = "none";
    var username = document.getElementById("loginTeamNameInput").value;
    var password = document.getElementById("loginTeamPassInput").value;
    send('{"messageType":"LoginMsg","sender":"0","payload":"{\\"username\\":\\"' + username + '\\",\\"password\\":\\"' + password + '\\"}"}');
}

function loginResult(value) {
    if (value == "200") {
        loggedTeam = document.getElementById("loginTeamNameInput").value;
        localStorage.setItem('loggedTeam', loggedTeam);
        showLoggedTeamName();
        showSelectTreasureHuntContainer();
        if (isThRunning()) {
            showCurrentTreasureHuntPage();
        } else {
            requireTHlist();
        }
    } else {
        showNotLoggedUser();
        alert("Wrong credentials");
    }
}

function logoutTeam() {
    var r = confirm("Logout?");
    if (r == true) {
        if (isThRunning()) {
            unsubscribeTreasureHunt();
        }
        loggedTeam = "";
        localStorage.removeItem('loggedTeam');
        currentHunt = "";
        localStorage.removeItem('currentHunt');
        currentHuntName = "";
        localStorage.removeItem('currentHuntName');
        poiToBeSent = "";
        localStorage.removeItem('poiToBeSent');
        poiToReach = "";
        localStorage.removeItem('poiToReach');
        latitudeToReach = "";
        localStorage.removeItem('latitudeToReach');
        currentQuiz = "";
        localStorage.removeItem('currentQuiz');
        currentQuizAnswer = "";
        localStorage.removeItem('currentQuizAnswer');
        loggedTeamID = 0;
        localStorage.removeItem('loggedTeamID');
        thID = 0;
        localStorage.removeItem('thID');
        clueID = 0;
        localStorage.removeItem('clueID');
        showNotLoggedUser();
    }
}

function unsubscribeTreasureHunt() {
    send('{"messageType":"UnsubscriptionMsg","sender":"' + loggedTeamID + '","payload":"' + thID + '"}');
    console.log('Unsubscription from "' + currentHuntName + '" done!')
}

