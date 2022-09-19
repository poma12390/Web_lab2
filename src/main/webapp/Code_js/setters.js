new_r_value()
function new_r_value() {
    value_R = parseFloat(document.getElementById("r_value").innerText.split("=")[1])
    let z ="";
    if (value_R !== 0 && value_R !== "") {

        if ((""+value_R).length===1) {
            set_R_value("b" + value_R)
        } else {
            console.log(""+value_R)
            z = (""+value_R).split(".")
            console.log("b" + z[0] + "_" + z[1] + " setter")
            set_R_value("b" + z[0] + "_" + z[1])
        }
    }
}
function new_y_value(){
    value_Y=parseFloat(document.getElementById("x_value").innerText.split("=")[1])
}
function new_x_value() {
    value_X = parseFloat(document.getElementById("y_value").innerText.split("=")[1])
}
document.addEventListener('DOMContentLoaded', function () {
    new_x_value()
    new_y_value()
    console.log(value_X, value_Y + " x, y ")

    console.log(value_R + " R")
    drawPoint()
});
function set_R_value(id){
    value_R=document.getElementById(id).getAttribute("value")
    document.getElementById("r_value").innerText = "R = " + value_R
    document.getElementById("X_rez").setAttribute("value",value_R )
    createSMTH()
}

function set_Y_value(id){
    id = document.getElementById(id)
    value_Y = (id.getAttribute("value"))
    document.getElementById("x_value").innerText = "Y = "+value_Y
}