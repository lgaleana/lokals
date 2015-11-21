var Chat = React.createClass({displayName: "Chat",
	render: function() {
		return (
			React.createElement("div", null, 
				"Hello!"
			)
		);
	}
});

React.render(React.createElement(Chat, null), document.getElementById("content"));
