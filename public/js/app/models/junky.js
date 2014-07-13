define([ 'underscore', 'backbone' ], function(_, Backbone) {

	Junky = Backbone.Model.extend({
		urlRoot : '/rest/junky',
		defaults : {
			name : 'none',
			count : 0,
			remaining : 0
		},
		initialize : function() {

		},
		countMate : function() {
			this.set("count", this.get("count") + 1);
			this.set("remaining", this.get("remaining") - 1);
		}

	});
	
	return Junky;
});
