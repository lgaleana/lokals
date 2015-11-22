window.Chat = function(streamUrl, postUrl) {
  (function() {
    var user = prompt("What should your user name be");
    $("input").on("keyup", function(e) {
      if( e.keyCode != 13)
        return;
      var m = { message: this.value, user: user };
      this.value = "";
      $.ajax({
        url: postUrl,
        data: JSON.stringify(m),
        method: "post",
        contentType: "application/json"
      });
    });

    var feed = new EventSource(streamUrl);
    feed.addEventListener("message", function (msg) {
      var m = JSON.parse(msg.data);
      $(".log" ).append("<div class='message'>" + m.user + ": " + m.message + "</div>");
    }, false);
  })();
}