
function renderQuestion() {
    var question = document.getElementById("question").innerHTML.trim();
    if(question !== "") {
        document.getElementById("display").innerHTML = question;
    } else {
        document.getElementById("display").innerHTML = document.getElementById("id").innerHTML;
    }

    var notes = document.getElementById("notes").innerHTML.trim();
    if (notes.indexOf("#") !== -1) {
        document.getElementById("display").innerHTML += "<br><br><div style='font-size: 0.8em'>" + notes + "</div>" + "\n\n"; // "<div id='notes-addition'>"+notes+"</div>"
    }
}

renderQuestion();
