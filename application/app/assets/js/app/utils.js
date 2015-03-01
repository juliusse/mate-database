define([], function() {
	
	var Utils = {};
	Utils.cancelDefaultAction =  function (e) {
		 var evt = e ? e:window.event;
		 if (evt.preventDefault) evt.preventDefault();
		 evt.returnValue = false;
		 return false;
	};
	return Utils;
});
