//--------------------------GLOBAL-VARIABLES--------------------------------
var lastClueText = "text text text text text text text text text text text text text text text text text text ";
var loggedTeam;
var currentHunt;
var save;
var app = {
    // Application Constructor
    initialize: function () {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);

        //load saved data
        loggedTeam = localStorage.getItem('loggedTeam');
        currentHunt = localStorage.getItem('currentHunt');

        //check if logged team exists
        if (loggedTeam != null) {
            showLoggedTeamName();
        } else {
            showNotLoggedUser();
        }

        //check if a TH is running
        if (currentHunt != null) {
            showCurrentTreasureHunt();
        } else {
            showNearTreasureHunt()
        }
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function () {
        //this.receivedEvent('deviceready');
        this.qrcode();
        this.writeCode();
        this.back();
        this.save();
        this.resume();
        this.leave();

    },


    // Update DOM on a Received Event
    qrcode: function () {
        document.getElementById("scanqr").onclick = function () {
        }
    },
    writeCode: function () {
        document.getElementById("writecode").onclick = function () {
            document.getElementById("insertCodePage").style.display = "none";
            document.getElementById("insertCodeManuallyPage").style.display = "block";
            document.getElementById("currentTreasureHunt").style.display = "none";
        }
    },
    back: function () {
        var cancelButton = document.getElementById("cancelCodeButton");
        cancelButton.onclick = function () {
            document.getElementById("insertCodePage").style.display = "block";
            document.getElementById("insertCodeManuallyPage").style.display = "none";
        }
    },
    save: function () {
        var saveButton = document.getElementById("saveCodeButton");
        saveButton.onclick = function () {
            codeSave = document.getElementById("insertCodeInput").value;
            alert("Saved treasure hunt");
            document.getElementById("hunt").innerHTML = codeSave;
            document.getElementById("insertCodePage").style.display = "block";
            document.getElementById("insertCodeManuallyPage").style.display = "none";
            document.getElementById("currentTreasureHunt").style.display = "block";
        }
    },
    resume: function () {
        var resume = document.getElementById("resumeButton");
        resume.onclick = function () {
            document.getElementById("insertCodePage").style.display = "none";
            document.getElementById("mapPage").style.display = "block";
            document.getElementById("currentTreasureHunt").style.display = "none";
            //set title of current treasure hunt
        }
    },
    leave: function () {
        var leave = document.getElementById("leaveButton");
        leave.onclick = function () {
            localStorage.removeItem('currentHunt');
            currentHunt="";
            document.getElementById("currentTreasureHunt").style.display = "none";
            showNearTreasureHunt();
        }
    }

};
//-----------------------------select-tho-----------------------------------
function showCurrentTreasureHunt(){
    document.getElementById("currentTreasureHunt").style.display = "block";
    document.getElementById("hunt").innerHTML = currentHunt;
}
function showNearTreasureHunt(){
    var div = document.getElementById("nearTreasureHunt");
    var content="<ul>";
    content+='<li onclick=\'showScanButtons("available")\' >available</li>';
    content+='<li onclick=\'showScanButtons("hunt")\' >hunt</li>';
    content+='<li onclick=\'showScanButtons("list")\' >list</li>';
    div.innerHTML=content+"</ul>";
    div.style.display = "block";
}
function showScanButtons(selectedTH){
    document.getElementById("nearTreasureHunt").style.display = "none";
    document.getElementById("scanqr").onClick=scanQrCode(""+selectedTH);
    document.getElementById("codeButtons").style.display = "block";
}
function scanQrCode(selectedTH){
    cordova.plugins.barcodeScanner.scan(
        function (result) {
            if (true){//TODO check if result is valid
                currentHunt=selectedTH;
                localStorage.setItem("currentHunt", selectedTH)
                document.getElementById("insertCodePage").style.display = "none";
                loadMapPage();
            }
        },
        function (error) {
            alert("Scanning failed: " + error);
        }
    );
}

//-------------------------------MAP----------------------------------------
function loadMapPage() {
    var mapScript = document.createElement('script');
    mapScript.setAttribute('src', 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBNQr4YcrvttSMIgWOX68kJnigaI0Cir9c&callback=mapLoadedCallback');
    document.head.appendChild(mapScript);
    document.getElementById("mapPageTitle").innerText = currentHunt;
    document.getElementById("mapPage").style.display = "block";
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
                map.setCenter(new google.maps.LatLng(latitude, longitude));
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
    document.getElementById("insertCodePage").style.display = "block";
    document.getElementById("codeButtons").style.display = "none";
    var huntName = document.getElementById("hunt").innerText;
    if (huntName != "") {
        document.getElementById("currentTreasureHunt").style.display = "block";
    }
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
    var container = document.getElementById("map-page-clue");
    var content = '<h1>CLUE</h1>';
    content += '<p>' + clueText + '</p>';
    content += '<div onClick="closeClue()" class="clue-quiz-button"><span>HIDE</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function closeClue() {
    document.getElementById("map-page-clue").style.display = "none";
}

//---------------------------------QUIZ------------------------------------

function showQuiz(question, answer) {
    var container = document.getElementById("map-page-quiz");
    var content = '<h1>QUIZ</h1>';
    content += '<p>' + question + '</p>';
    content += '<input class="quizInput" id="quizInput" type="text" />'
    content += '<div onClick="checkQuiz(\'' + answer + '\')" class="clue-quiz-button"><span>CHECK</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function checkQuiz(answer) {
    if (document.getElementById("quizInput").value.trim() == answer) {
        document.getElementById("map-page-quiz").style.display = "none";
    }
}

//---------------------------------PROGRESS---------------------------------
function viewProgress() {
    getProgress(function (totalPOI, reachedPOI) {
        var div = document.getElementById("map-page-progress");
        var content = "";
        var value = totalPOI / reachedPOI * 10 + 20;
        content += '<div class="closeProgress" onclick="closeProgress()">X</div><div class="progressPercent">' + Math.round(value) + '%</div><div class="outOfProgress">' + reachedPOI + '</br>POI</br>reached</br>out of</br>' + totalPOI + '</div><div class="progressBar"><div class="progressBarContent animated bounceInDown" style="height:' + value + '%;"></div></div>';
        div.innerHTML = content;
        div.style.display = "block";
    });
}

function getProgress(callback) {
    var totalPOI = 10;
    var reachedPOI = 3;
    callback(totalPOI, reachedPOI);
}

function closeProgress() {
    document.getElementById("map-page-progress").style.display = "none";
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
    if (document.getElementById("createTeamPass1Input").value == document.getElementById("createTeamPass2Input").value) {
        //TODO create team
    }
    showLoggedTeamName()
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
    showLoggedTeamName()
}

function logoutTeam() {
    document.getElementById("createOrLogin").style.display = "block";
}

app.initialize();