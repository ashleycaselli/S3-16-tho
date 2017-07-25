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

document.addEventListener('deviceready', init(), false);

function init(){
    document.getElementById("second").style.display = "none";
    document.getElementById("third").style.display = "none";
    document.getElementById("scanqr").onclick = function () {
            document.getElementById("first").style.display = "none";
            document.getElementById("second").style.display = "block";

    }
    document.getElementById("writec").onclick = function () {
                document.getElementById("first").style.display = "none";
                document.getElementById("second").style.display = "none";
                document.getElementById("third").style.display = "block";
    }
}

//-------------------------------CLUE----------------------------------------

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
}
function checkQuiz(answer){
    if(document.getElementById("quizInput").value.trim()==answer){
        document.getElementById("map-page-quiz").style.display="none";
    }
}