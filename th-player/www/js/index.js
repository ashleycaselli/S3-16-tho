/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
//--------------------------GLOBAL-VARIABLES--------------------------------
var lastClueText = "text text text text text text text text text text text text text text text text text text ";
var loggedTeam;
var save;
var codeSave;
var app = {
    // Application Constructor
    initialize: function () {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
        loggedTeam = localStorage.getItem('loggedTeam');
        if (loggedTeam != null) {
            showLoggedTeamName();
        } else {
            showNotLoggedUser();
        }
        if (codeSave != null) {
            document.getElementById("description").style.display = "block";
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
        this.leave();
        this.ok();

    },


    // Update DOM on a Received Event
    /*receivedEvent: function (id) {
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
        document.getElementById("first").style.display = "none";
        document.getElementById("second").style.display = "block";


    },*/
    qrcode: function () {
        document.getElementById("scanqr").onclick = function () {
            cordova.plugins.barcodeScanner.scan(
                function (result) {
                    alert("We got a barcode\n" +
                        "Result: " + result.text + "\n" +
                        "Format: " + result.format + "\n" +
                        "Cancelled: " + result.cancelled);
                },
                function (error) {
                    alert("Scanning failed: " + error);
                })
            document.getElementById("second").style.display = "block";
            document.getElementById("first").style.display = "none";
            document.getElementById("load").style.display = "none";
            loadSecondPage();

        }
    },
    writeCode: function () {
        document.getElementById("writecode").onclick = function () {
            document.getElementById("first").style.display = "none";
            document.getElementById("third").style.display = "block";
            document.getElementById("load").style.display = "none";
        }
    },
    back: function () {
        var back = document.getElementsByClassName("back");
        back[0].onclick = function () {
            document.getElementById("first").style.display = "block";
            document.getElementById("third").style.display = "none";
            document.getElementById("load").style.display = "none";
        }
    },
    save: function () {
        save = document.getElementsByClassName("save");
        save[0].onclick = function () {
            codeSave = document.getElementById("insertCode").value;
            alert("Saved treasure hunt");
            document.getElementById("hunt").innerHTML = codeSave;
            document.getElementById("first").style.display = "block";
            document.getElementById("third").style.display = "none";
            document.getElementById("description").style.display = "block";
            document.getElementById("load").style.display = "none";
        }
    },
    leave: function () {
        var leave = document.getElementsByClassName("buttonLeave");
        leave[0].onclick = function () {
            document.getElementById("first").style.display = "block";
            document.getElementById("description").style.display = "none";
            delete codeSave;
        }
    },
    ok:function () {
        document.getElementById("ok").onclick = function () {
            document.getElementById("first").style.display = "block";
            document.getElementById("load").style.display = "none";
        }

    }

};

//-------------------------------MAP----------------------------------------
function loadSecondPage() {
    var mapScript = document.createElement('script');
    mapScript.setAttribute('src', 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBNQr4YcrvttSMIgWOX68kJnigaI0Cir9c&callback=mapLoadedCallback');
    document.head.appendChild(mapScript);
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
<<<<<<< HEAD
    content += '<p>' + question + '</p>';
    content += '<input class="quizInput" id="quizInput" type="text" />'
    content += '<div onClick="checkQuiz(\'' + answer + '\')" class="clue-quiz-button"><span>CHECK</span></div>';
    container.innerHTML = content;
    container.style.display = "block";
}

function checkQuiz(answer) {
    if (document.getElementById("quizInput").value.trim() == answer) {
        document.getElementById("map-page-quiz").style.display = "none";
=======
    content += '<p>'+question+'</p>';
    content+= '<input class="quizInput" id="quizInput" type="text" />'
    content += '<div onClick="checkQuiz(\''+answer+'\')" class="clue-quiz-button"><span>CHECK</span></div>';
    container.innerHTML=content;
    container.style.display="block";
    document.getElementById("description").style.display="none";
    document.getElementById("load").style.display = "none";
}
function checkQuiz(answer){
    if(document.getElementById("quizInput").value.trim()==answer){
        document.getElementById("map-page-quiz").style.display="none";
>>>>>>> 9a2ed5f9f387fe79b94def0e6a5105f170c7bae9
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
    document.getElementById("notLoggedUser").style.display = "block";
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
    document.getElementById("notLoggedUser").style.display = "none";
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
    document.getElementById("notLoggedUser").style.display = "none";
    //TODO check correct data
    loggedTeam = document.getElementById("loginTeamNameInput").value;
    showLoggedTeamName()
}

function logoutTeam() {
    document.getElementById("createOrLogin").style.display = "block";
}

app.initialize();
