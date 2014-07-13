define([ 'underscore', 'backbone' ], function(_, Backbone) {

	Junky = Backbone.Model.extend({
		urlRoot : '/rest/junky',
		defaults : {
			name : 'none',
			count : 0,
			credit : 0.0
		},
		initialize : function() {

		},
		countMate : function() {
			this.set("count", this.get("count") + 1);
			this.set("credit", this.get("credit") - 0.75);
		}

	});
	
	return Junky;
});
