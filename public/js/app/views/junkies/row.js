define([
// These are path alias that we configured in our bootstrap
'jquery', // lib/jquery/jquery
'underscore', // lib/underscore/underscore
'backbone', 'app/models/junky', 'text!templates/junkyRow.html',
		'image!/assets/dynamic/image1', 'image!/assets/dynamic/image2',
		'image!/assets/dynamic/image3' ], function($, _, Backbone, Junky, html,
		img1, img2, img3) {
	function wishGoodThirst(mateButton) {
		var origText = $(mateButton).html();
		var origClass = mateButton.className;
		var origOnClick = mateButton.onclick;

		$(mateButton).html(":-)");
		$(mateButton).toggleClass("btn-success");
		$(mateButton).toggleClass("btn-warning");
		mateButton.onclick = "";

		setTimeout(function() {
			$(mateButton).html(origText);
			$(mateButton).toggleClass("btn-success");
			$(mateButton).toggleClass("btn-warning");
			mateButton.onclick = origOnClick;
		}, 3000);
	}

	function countMate(event) {
		var junkyId = $(event.currentTarget).data("user");
		var junky = new Junky({
			id : junkyId
		});

		junky.fetch({
			success : function(junky) {
				junky.countMate();
				junky.save({}, {
					success : function(junky) {
						// TODO update row
						new JunkyRowView({
							el : $("#row-" + junky.get("id")),
							junky : junky
						});
						// TODO wish good thirst
					}

				});
			}
		});
	}

	function createCheckList(junky) {
		var list = [];

		var bottles = junky.get("count");
		var level3Images = Math.floor(bottles / 160);
		var level2Images = Math.floor((bottles % 160) / 20);
		var level1Images = bottles % 20;

		for (var i = 0; i < level3Images; i++) {
			list.push(img3.cloneNode());
			list.push(" ");
		}
		for (var i = 0; i < level2Images; i++) {
			list.push(img2.cloneNode());
			list.push(" ");
		}
		for (var i = 0; i < level1Images; i++) {
			list.push(img1.cloneNode());
			list.push(" ");
		}

		return list;

	}

	JunkyRowView = Backbone.View.extend({
		defaults : {
			junky : null
		},
		initialize : function(data) {
			this.render(data.junky);
		},
		render : function(junky) {
			// Compile the template using underscore
			var data = {
				junky : junky,
			};
			var template = _.template(html, data);
			// Load the compiled HTML into the Backbone "el"
			this.$el.html(template);

			this.$el.find("td.pictures").html(createCheckList(junky));
			this.$el.find("button.countButton")[0].onclick = countMate;
		}
	});

	return JunkyRowView;

});