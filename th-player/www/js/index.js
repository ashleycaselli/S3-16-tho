//--------------------------GLOBAL-VARIABLES--------------------------------
var lastClueText = "text text text text text text text text text text text text text text text text text text ";
var loggedTeam;
var currentHunt;

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    //load saved data
    loggedTeam = localStorage.getItem('loggedTeam');
    currentHunt = localStorage.getItem('currentHunt');

    //check if logged team exists
    if (loggedTeam != null) {
        showLoggedTeamName();
        if (currentHunt != null) {
            showCurrentTreasureHunt();
        } else {
            showNearTreasureHunt()
        }
    } else {
        showNotLoggedUser();
    }

    stompJS("test");
}

function stompJS(message) {
    var ws = new WebSocket('ws://52.14.140.101:15674/ws');
    var client = Stomp.over(ws);
    var on_connect = function () {
        console.log('connected');
        client.send('/amq/queue/rpc-queue', {"content-type": "text/plain"}, message);
        alert("sent");
        // var subscription = client.subscribe("/queue/test", callback);
    };
    var on_error = function () {
        alert('error');
    };
    client.connect('guest', 'guest', on_connect, on_error, "/");
}

function resumeTreasureHunt() {
    document.getElementById("selectTreasureHuntPage").style.display = "none";
    document.getElementById("mapPage").style.display = "block";
    document.getElementById("mapPageTitle").innerText = currentHunt;
    document.getElementById("currentTreasureHunt").style.display = "none";
}

function leaveTreasureHunt() {
    localStorage.removeItem('currentHunt');
    currentHunt = "";
    document.getElementById("currentTreasureHunt").style.display = "none";
    showNearTreasureHunt();
}

//-----------------------------select-tho-----------------------------------
function showCurrentTreasureHunt() {
    document.getElementById("currentTreasureHunt").style.display = "block";
    document.getElementById("treasureHuntName").innerHTML = currentHunt;
}

function showNearTreasureHunt() {
    var div = document.getElementById("nearTreasureHunt");
    var content = "<ul>";
    content += '<li onclick=\'showScanButtons("Available")\' >Available</li>';
    content += '<li onclick=\'showScanButtons("Hunt")\' >Hunt</li>';
    content += '<li onclick=\'showScanButtons("List")\' >List</li>';
    div.innerHTML = content + "</ul>";
    div.style.display = "block";
}

function showScanButtons(selectedTH) {
    document.getElementById("nearTreasureHunt").style.display = "none"
    document.getElementById("scanQR").onclick = function () {
        scanQRCode("" + selectedTH)
    };
    document.getElementById("writeCode").onclick = function () {
        writeQRCode("" + selectedTH)
    };
    document.getElementById("codeButtonsLabel").innerHTML = selectedTH;
    document.getElementById("codeButtons").style.display = "block";
}

function scanQRCode(selectedTH) {
    cordova.plugins.barcodeScanner.scan(
        function (result) {
            checkScanResults(selectedTH, result.text);
        },
        function (error) {
            alert("Scanning failed: " + error);
        }
    );
}

function writeQRCode(selectedTH) {
    document.getElementById("codeButtons").style.display = "none";
    document.getElementById("saveCodeButton").onclick = function () {
        checkScanResults(selectedTH, document.getElementById("insertCodeInput").value);
        document.getElementById("insertCodeManually").style.display = "none";
    }
    document.getElementById("cancelCodeButton").onclick = function () {
        document.getElementById("insertCodeManually").style.display = "none";
        document.getElementById("codeButtons").style.display = "block";
    }
    document.getElementById("insertCodeInput").value = "";
    document.getElementById("insertCodeManually").style.display = "block";
    document.getElementById("insertCodeInput").focus()
}

function checkScanResults(selectedTH, result) {
    if (true) {//TODO check if result is valid
        currentHunt = selectedTH;
        localStorage.setItem("currentHunt", selectedTH)
        document.getElementById("selectTreasureHuntPage").style.display = "none";
        loadMapPage();
    }
}

//-------------------------------MAP----------------------------------------
function loadMapPage() {
    document.getElementById("mapPage").style.display = "block";
    document.getElementById("googleMap").style.display = "block";
    document.getElementById("mapPageTitle").innerText = currentHunt;
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBNQr4YcrvttSMIgWOX68kJnigaI0Cir9c&callback=mapLoadedCallback';
    document.head.appendChild(script);
}

function mapLoadedCallback() {
    alert("cb");
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
            })
        }, 3000);
    })
}

function getLocation(callback) {
    var onSuccess = function (position) {
        callback(position.coords.latitude, position.coords.longitude)
    };

    function onError(error) {
        alert('code: ' + error.code + '\n' +
            'message: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(onSuccess, onError);
}

function exitFromMap() {
    document.getElementById("mapPage").style.display = "none";
    document.getElementById("selectTreasureHuntPage").style.display = "block";
    document.getElementById("codeButtons").style.display = "none";
    document.getElementById("treasureHuntName").innerHTML = currentHunt;
    document.getElementById("currentTreasureHunt").style.display = "block";
}

//-------------------------------CLUE----------------------------------------
function showLastClue() {
    showClue(lastClueText);
}

function showNewClue(text) {
    showClue(text);
    setLastClueText(text);
}

function setLastClueText(text) {
    var lastClueText = text;
}

function showClue(clueText) {
    var container = document.getElementById("mapPageClue");
    var content = '<h1>CLUE</h1>';
    content += '<p>' + clueText + '</p>';
    content += '<div onClick="closeClue()" class="clue-quiz-button"><span>HIDE</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function closeClue() {
    document.getElementById("mapPageClue").style.display = "none";
}

//---------------------------------QUIZ------------------------------------

function showQuiz(question, answer) {
    var container = document.getElementById("mapPageQuiz");
    var content = '<h1>QUIZ</h1>';
    content += '<p>' + question + '</p>';
    content += '<input class="quiz-input" id="quizInput" type="text" />'
    content += '<div onClick="checkQuiz(\'' + answer + '\')" class="clue-quiz-button"><span>CHECK</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function checkQuiz(answer) {
    if (document.getElementById("quizInput").value.trim() === answer) {
        document.getElementById("mapPageQuiz").style.display = "none";
    }
}

//---------------------------------PROGRESS---------------------------------
function viewProgress() {
    getProgress(function (totalPOI, reachedPOI) {
        var div = document.getElementById("mapPageProgress");
        var content = "";
        var value = totalPOI / reachedPOI * 10 + 20;
        content += '<div class="close-progress" onclick="closeProgress()">X</div><div class="progress-percent">' + Math.round(value) + '%</div><div class="out-of-progress">' + reachedPOI + '</br>POI</br>reached</br>out of</br>' + totalPOI + '</div><div class="progress-bar"><div class="progress-bar-content animated bounceInDown" style="height:' + value + '%;"></div></div>';
        div.innerHTML = content;
        div.style.display = "block";
    });
}

function getProgress(callback) {
    var totalPOI = 10;
    var reachedPOI = 4;
    callback(totalPOI, reachedPOI);
}

function closeProgress() {
    document.getElementById("mapPageProgress").style.display = "none";
}

//---------------------------------LOGIN---------------------------------
function showLoggedTeamName() {
    userInfo = document.getElementById("userInfo");
    userInfo.innerHTML = '<span>Team: ' + loggedTeam + '</span>';
    userInfo.style.display = "block";
}

function showNotLoggedUser() {
    document.getElementById("notLoggedUserPage").style.display = "block";
    document.getElementById("createOrLogin").style.display = "block";
    document.getElementById("loginTeam").style.display = "none";
    document.getElementById("createTeam").style.display = "none";
}

function showCreateTeamPage() {
    document.getElementById("createOrLogin").style.display = "none";
    document.getElementById("createTeam").style.display = "block";
    document.getElementById("createTeamNameInput").focus();
}

function createTeam() {
    document.getElementById("notLoggedUserPage").style.display = "none";
    loggedTeam = document.getElementById("createTeamNameInput").value;
    if (document.getElementById("createTeamPassInput").value === document.getElementById("createTeamConfirmPassInput").value) {
        //TODO create team
    }
    showLoggedTeamName()
    //check if a TH is running
    if (currentHunt != null) {
        showCurrentTreasureHunt();
    } else {
        showNearTreasureHunt()
    }
}

function showLoginTeamPage() {
    document.getElementById("createOrLogin").style.display = "none";
    document.getElementById("loginTeam").style.display = "block";
    document.getElementById("loginTeamNameInput").focus();
}

function loginTeam() {
    document.getElementById("notLoggedUserPage").style.display = "none";
    //TODO check correct data
    loggedTeam = document.getElementById("loginTeamNameInput").value;
    localStorage.setItem('loggedTeam', loggedTeam);
    showLoggedTeamName()
    //check if a TH is running
    if (currentHunt != null) {
        showCurrentTreasureHunt();
    } else {
        showNearTreasureHunt()
    }
}

function logoutTeam() {
    document.getElementById("createOrLogin").style.display = "block";
}
