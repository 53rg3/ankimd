
function renderQuestion() {
    var question = document.getElementById("question").innerHTML.trim();
    if(question !== "") {
        document.getElementById("display").innerHTML = question;
    } else {
        document.getElementById("display").innerHTML = document.getElementById("id").innerHTML;
    }
}

renderQuestion();
