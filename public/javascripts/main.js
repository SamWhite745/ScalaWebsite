let clicker = document.getElementById("clicker");
let highscore = document.getElementById("highscore");
let currentScore = document.getElementById("currentscore");
let total = 0;
let currentHighScore = 0;
let running = true;

clicker.addEventListener("click", () => {
    total++;
    console.log("total: " + total);
    console.log("highscore: " + currentHighScore);
    if (running) setTimeout(fancyDisable, 5000);
    running = false;
});

let enabler = () => {
    clicker.setAttribute("class", "MyButton");
    clicker.disabled = false;
    total = 0;
    running = true;
};

let fancyDisable = () => {
    currentScore.innerText = "Current: " + total;
    if (total > currentHighScore) {
        currentHighScore = total;
        highscore.innerText = "Highscore: " + currentHighScore;
    }
    clicker.setAttribute("class", "MyButton activeButton");
    clicker.disabled = true;
    setTimeout(enabler, 5000);
};
