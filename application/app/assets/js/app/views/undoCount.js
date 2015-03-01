define([
// These are path alias that we configured in our bootstrap
'jquery', // lib/jquery/jquery
'underscore', // lib/underscore/underscore
'backbone', 'app/models/junky', 'text!templates/undo.html' ],
		function($, _, Backbone, Junky, UndoView, html) {

			undoView = Backbone.View.extend({
				defaults : {
					junky: null
				},
				initialize : function(data) {
					this.render(data.junky);
				},
				undo : function() {

					return false;
				},
				events : {
					"click a#undoCount-link" : "undo",
				},
				render : function(junky) {
					// Compile the template using underscore
					var data = {junky: junky};
					var template = _.template(html, data);
					// Load the compiled HTML into the Backbone "el"
					this.$el.html(template);
				}
			});

			return undoView;

		});