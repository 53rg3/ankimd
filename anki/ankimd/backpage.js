var converter = new showdown.Converter({tables: true, strikethrough: true});

function renderHtml() {

    var markdown = document.getElementById("markdown").innerHTML;
    markdown = markdown
        .replace(/<br>/g, "\n")
        .replace(/^---.*?---/gms, "")
        .replace(/\\#/gms, "#");

    var notes = document.getElementById("notes").innerHTML.trim();
    if (notes !== "") {
        markdown = "<div id='notes-addition'>" + notes + "</div>" + "\n\n" + markdown; // "<div id='notes-addition'>"+notes+"</div>"
    }

    document.getElementById("html").innerHTML = converter.makeHtml(markdown);
}

renderHtml();
