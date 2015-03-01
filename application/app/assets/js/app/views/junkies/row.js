define(
		[
		// These are path alias that we configured in our bootstrap
		'jquery', // lib/jquery/jquery
		'jquerymeta', 'jquerytable', 'underscore', // lib/underscore/underscore
		'backbone', 'app/models/junky', 'app/views/undoCount', 'text!templates/junkyRow.html',
				'image!/assets/dynamic/image1', 'image!/assets/dynamic/image2',
				'image!/assets/dynamic/image3' ],
		function($, meta, table, _, Backbone, Junky, UndoView, html, img1, img2, img3) {
			function wishGoodThirst(mateButton) {
				var origText = $(mateButton).html();

				$(mateButton).html(":-)");
				$(mateButton).toggleClass("btn-success");
				$(mateButton).toggleClass("btn-warning");

				setTimeout(function() {
					$(mateButton).html(origText);
					$(mateButton).toggleClass("btn-success");
					$(mateButton).toggleClass("btn-warning");
				}, 3000);
			}

			function countMate(event) {
				var junkyId = $(event.currentTarget).data("user");
				var junky = MateDatabase.junkies.find(function(item) {
					return item.get("id") == junkyId;
				});

				junky.fetch({
					success : function(junky) {
						junky.countMate();
						junky.save({}, {
							success : function(junky) {
								// update view
								new JunkyRowView({
									el : $("#junky-container"),
									junky : junky
								});

								// update total count
								var count = MateDatabase.junkies.totalCount();
								$("#totalCount").text(count);

								wishGoodThirst($("#row-" + junky.get("id"))
										.find("button.countButton"));

								$("#junkyTable").trigger('update');
								$("#junkyTable").tablesorter({
									sortList : [ [ 3, 1 ] ],
									headers : {
										3 : {
											sorter : 'digit'
										}
									}
								});
							}
						});
						
						//create undo view
						new UndoView({
							el : $("#undoCount-container"),
							junky : junky
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

			JunkyRowView = Backbone.View
					.extend({
						defaults : {
							junky : null
						},
						initialize : function(data) {
							this.render(data.junky);
						},
						render : function(junky) {
							$("#row-" + junky.get("id")).remove();
							// Compile the template using underscore
							var data = {
								junky : junky
							}
							var template = _.template(html, data);
							// Load the compiled HTML into the Backbone "el"
							this.$el.append(template);

							$("#row-" + junky.get("id")).find("td.pictures")
									.html(createCheckList(junky));
							$("#row-" + junky.get("id")).find(
									"button.countButton")[0].onclick = countMate;
						}
					});

			return JunkyRowView;

		});