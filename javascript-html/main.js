var client = new WindowsAzure.MobileServiceClient(
    "https://murcs.azure-mobile.net/",
    "DKnzMvQWhglLdTMeObLCxixqLMKcaG57"
);

var userTable = client.getTable("User");
var tasksTable = client.getTable("Task");


var refreshTasks = function () {
    tasksTable.orderByDescending("CreatedAt").read().done(function (results) {
        buildTasks(results);
    });
};

var refreshUsers = function () {
    userTable.where({ status: "online" })
                .read()
                .done(function (results) {
                    buildParticipants(results);
                });
};

var buildParticipants = function (users) {

    $("#collaborators").html("");
    for (var i = 0; i < users.length; i++)
    {
        var user = users[i];

        var collaborator = $("<div/>");
        collaborator.addClass("collaborator");
        collaborator.attr("data-id", user.id);

        var image = $("<img/>");
        image.attr({ src: user.Image, width: "65px" });
        image.addClass("image");

        var name = $("<div/>");
        name.addClass("name");
        name.html(user.UserName);

        collaborator.append(image);
        collaborator.append(name);

        var clear = $("<div/>");
        clear.addClass("clear");

        //collaborator.hide();

        $("#collaborators").append(collaborator);
        $("#collaborators").append(clear);

        //collaborator.fadeIn();
    }

};

var buildTasks = function (tasks) {

    $("#tasks").html("");

    for (var i = 0; i < tasks.length; i++)
    {
        var task = tasks[i];

        var div = $("<div/>");
        div.addClass("task");
        div.attr("data-id", task.id);

        var text = $("<div/>");
        text.addClass("task-text");
        text.html(task.Text);

        var imageDiv = $("<div/>");
        imageDiv.addClass("task-image");

        var img = $("<img/>");
        img.attr("src", "");

        div.append(text);
        
        imageDiv.append(img);

        div.append(imageDiv);

        //div.hide();
        $("#tasks").append(div);

        //div.fadeIn();
    }
};

refreshTasks();
refreshUsers();

var timer = function () {
    setTimeout(function () {
        console.log("Refreshing..");
        refreshTasks();
        refreshUsers();
        
        timer();
    }, 5000);
}

timer();