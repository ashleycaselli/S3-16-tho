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
var lastClueText="text text text text text text text text text text text text text text text text text text ";
var save;
var codeSave;
var app = {
    // Application Constructor
    initialize: function () {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
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
            document.getElementById("second").style.display="block";
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
//-------------------------------CLUE----------------------------------------
function loadSecondPage(){
    var mapScript = document.createElement('script');

    mapScript.setAttribute('src','https://maps.googleapis.com/maps/api/js?key=AIzaSyBNQr4YcrvttSMIgWOX68kJnigaI0Cir9c&callback=myMap');

    document.head.appendChild(mapScript);
}
function myMap() {
    var mapProp = {
        center: new google.maps.LatLng(44.13972, 12.24286),
        zoom: 15,
    };
    var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);

    var marker = new google.maps.Marker({
        position: {lat: 44.13972, lng: 12.24286},
        map: map,
        title: "Prova"
    });
    setInterval(function(){
    map.setCenter(new google.maps.LatLng( 44.13972, 12.24286 ) );
    marker.setPosition({lat: 44.13972, lng: 12.24286});
    }, 3000);
}
function showLastClue(){
    showClue(lastClueText);
}
function showNewClue(text){
    showClue(text);
    setLastClueText(text);
}
function setLastClueText(text){
    var lastClueText=text;
}
function showClue(clueText){
    var container = document.getElementById("map-page-clue");
    var content = '<h1>CLUE</h1>';
    content += '<p>'+ clueText +'</p>';
    content += '<div onClick="closeClue()" class="clue-quiz-button"><span>HIDE</span></div>';
    container.innerHTML=content;
    container.style.display="block";
}
function closeClue(){
    document.getElementById("map-page-clue").style.display="none";
}

//---------------------------------QUIZ------------------------------------

function showQuiz(question, answer){
    var container = document.getElementById("map-page-quiz");
    var content = '<h1>QUIZ</h1>';
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
    }
}

app.initialize();
