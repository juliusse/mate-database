define([ 'underscore', 'backbone', 'app/models/junky' ], function(_, Backbone,
		JunkyModel) {
	JunkyCollection = Backbone.Collection.extend({
		model : JunkyModel,
		comparator : function(model) {
			return model.get('count');
		},
		totalCount : function() {
			return this.models.map(function(junky) {
				return junky.get("count")
			}).reduce(function(oldVal, curVal) {
				return oldVal + curVal;
			});
		}
	});

	return JunkyCollection;
});
