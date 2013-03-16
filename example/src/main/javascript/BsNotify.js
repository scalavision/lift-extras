/* jshint unused:false */
var BsNotify = (function($) {
  "use strict";

  // private vars
  var settings = {
    selector: "#bs-notifier",
    closable: true,
    transition: "fade",
    fadeOut: {
      enabled: true,
      delay: 5000
    },
    ids: []
  };

  function bsPriority(it) {
    if (it === "notice") {
      return "info";
    }
    return it;
  }

  var inst = {};

  // public funcs
  inst.init = function(data) {
    settings = $.extend({}, settings, data);

    $(document).on("add-notices", function(event, data) {
      var notices = Array.prototype.slice.call(arguments, 1);
      $.each(notices, function(index, value) {
        inst.notify(value);
      });
    });

    $(document).on("clear-notices", function(event) {
      $(settings.selector).html("");
    });

    $.each(settings.ids, function(ix, noticeId) {
      $(document).on("set-notice-id-"+noticeId, function() {
        var notices = Array.prototype.slice.call(arguments, 1);
        $.each(notices, function(index, value) {
          inst.notify(value);
        });
      });
    });
  };

  inst.notify = function(msg) {
    $(settings.selector).notify({
      message: {
        html: msg.message
      },
      type: bsPriority(msg.priority),
      closable: settings.closable,
      transition: settings.transition,
      fadeOut: settings.fadeOut
    }).show();
  };

  return inst;
}(jQuery));
