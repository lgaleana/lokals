window.Chat = function(streamUrl, postUrl) {
  var ENTER = 13;
  var user = prompt("Por favor ingresa tu nombre");
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
    $(".log" ).append(messageDiv(m));
  }, false);

  var messageDiv = function(message) {
    if ( message.user === user )
      return "<div class='message user-message'>" + message.user + ": " + message.message + "</div>";
    else
      return "<div class='message'>" + message.user + ": " + message.message + "</div>";
  }
}
