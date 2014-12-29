define([
// These are path alias that we configured in our bootstrap
'jquery', // lib/jquery/jquery
'underscore', // lib/underscore/underscore
'backbone', 'app/models/junky', 'app/views/junkies/row', 'text!templates/newJunkyForm.html' ],
		function($, _, Backbone, Junky, JunkyView, html) {

			newUserFormView = Backbone.View.extend({
				defaults : {},
				initialize : function(data) {
					this.render();
				},
				addUser : function() {
					var input = $('input#newUser-name')[0];
					var username = input.value;
					input.value = "";

					var newJunky = new Junky({
						name : username
					});
					newJunky.save({}, {
						success : function(junky) {
							MateDatabase.junkies.add(junky);
							new JunkyView({
								el : $('#junky-container'),
								junky : junky
							});
						}
					});
				},
				events : {
					"click button#newUser-button" : "addUser",
				},
				render : function() {
					// Compile the template using underscore
					var data = {};
					var template = _.template(html, data);
					// Load the compiled HTML into the Backbone "el"
					this.$el.html(template);
				}
			});

			return newUserFormView;

		});