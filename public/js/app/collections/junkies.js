define([ 'underscore', 'backbone', 'app/models/junky' ], function(_, Backbone, JunkyModel) {
	JunkyCollection = Backbone.Collection.extend({
		model : JunkyModel,
		comparator : function(model) {
			return model.get('count');
		}
	});

	return JunkyCollection;
});
