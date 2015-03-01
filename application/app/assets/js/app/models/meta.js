define([ 'underscore', 'backbone' ], function(_, Backbone) {

	Junky = Backbone.Model.extend({
		  urlRoot: '/rest/meta/',/*function(){
			    if (this.isNew()){
			      return "/rest/meta";
			    } else {
			      return "/rest/meta/" + this.id;
			    }},*/
		defaults : {
			dbVersion : 0,
			bottlesAvailable : 0
		},
		initialize : function() {
		
		},
		refresh : function() {
			this.fetch({
				success: function() {
					$("#bottlesAvailable").text(MateDatabase.meta.get("bottlesAvailable"));
				}
			})
		}
		
	});
	
	return Junky;
});
