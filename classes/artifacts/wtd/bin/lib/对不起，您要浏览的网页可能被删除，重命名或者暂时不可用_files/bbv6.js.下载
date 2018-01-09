var SiteTracker = function(s, p, r, u) {
  if (s != undefined && s != null) {
    this.site = s;
  }

  if (p != undefined && p != null) {
    this.page = p;
  }

  if (r != undefined && r != null) {
    this.referer = r;
  }

  if (u != undefined && u != null) {
    this.uid = u;
  }

  this.serial = 0;
};

SiteTracker.prototype.getCookie = function(sKey) {
  if (!sKey || !this.hasItem(sKey)) { return null; }
  return decodeURIComponent(document.cookie.replace(new RegExp("(?:^|.*;\\s*)" + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*((?:[^;](?!;))*[^;]?).*"), "$1"));
};

SiteTracker.prototype.hasItem =  function (sKey) {
  return (new RegExp("(?:^|;\\s*)" + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
};

SiteTracker.prototype.track = function(t_params) {
  this.buildParams();
  if(t_params && t_params.h) this.params.h = t_params.h;
  var src = "";

  if (typeof(t_params) == "undefined" || typeof(t_params.target_url) == "undefined") {
    src = "//s.anjuke.com/stb?__site=" + this.params['site'] + "&";
  }
  else {
    src = t_params.target_url;
  }

  var prev_if = document.getElementById("sojtracker" + this.serial);
  while (prev_if) {
    this.serial += 1;
    prev_if = document.getElementById("sojtracker" + this.serial);
  }

  var ifContainer = document.createElement("div");
  ifContainer.innerHTML = '<iframe style="display:none" id="sojtracker' + this.serial + '" name="sojtracker' + this.serial + '" height="300" width="500"></iframe>';
  (document.getElementsByTagName('head')[0]).appendChild(ifContainer);

  var form = document.createElement("form");
  form.action = src;
  form.method = "post";
  for (var k in this.params) {
    if (k == "uid") {
      form.innerHTML += "<input type='hidden' name='" + k + "' value='" + (this.params[k] || 0) + "' />";
    }
    else {
      form.innerHTML += "<input type='hidden' name='" + k + "' value='" + (this.params[k] || "") + "' />";
    }
  }
  (document.getElementsByTagName('head')[0]).appendChild(form);
  form.target = "sojtracker" + this.serial;
  form.submit();
};

SiteTracker.prototype.buildParams = function() {
  var href  = document.location.href;

  var guid = this.getCookie(this.nGuid   || "aQQ_ajkguid");
  var ctid = this.nCtid || this.getCookie(this.nCtid   || "ctid");
  var luid = this.getCookie(this.nLiu    || "lui");
  var ssid = this.getCookie(this.nSessid || "sessid");
  var uid  = this.getCookie(this.nUid    || "ajk_member_id");

  if (this.uid != undefined && this.uid != null) {
    uid = this.uid;
  }

  if (uid == undefined || uid == null || uid == "") {
    uid = 0;
  }

  var method = "";
  if (this.method != undefined && this.method != null) {
    method = this.method;
  }

  this.params = new Object();
  this.params.p = this.page;
  this.params.h = href;
  this.params.r = this.referer;
  this.params.site = this.site;
  this.params.guid = guid;
  this.params.ssid = ssid;
  this.params.uid  = uid;
  this.params.t = new Date().getTime();
  this.params.ctid = ctid;
  this.params.luid = luid;
  this.params.m = method;

  if (this.screen != undefined) {
    this.params.sc = JSON.stringify(this.screen)
  }

  if (this.cst != undefined && /[0-9]{13}/.test(this.cst)) {
    this.params.lt = this.params.t - parseInt(this.cst);
  }

  if (this.pageName != undefined) {
    this.params.pn = this.pageName;
  }

  if (this.customParam != undefined) {
    this.params.cp = this.customParam;
  }

};

SiteTracker.prototype.setSite = function(s) {
  this.site = s;
};

SiteTracker.prototype.setPage = function(p) {
  this.page = p;
};

SiteTracker.prototype.setPageName = function(n) {
  this.pageName = n;
};

SiteTracker.prototype.setCookieNames = function(c) {
  this.cookNames = c;
};

SiteTracker.prototype.setReferer = function(r) {
  this.referer = r;
};

SiteTracker.prototype.setUid = function(u) {
  this.uid = u;
};

SiteTracker.prototype.setMethod = function(m) {
  this.method = m;
};

SiteTracker.prototype.setNGuid = function(n) {
  this.nGuid = n;
};

SiteTracker.prototype.setNCtid = function(n) {
  this.nCtid = n;
};

SiteTracker.prototype.setNLiu = function(n) {
  this.nLiu = n;
};

SiteTracker.prototype.setNSessid = function(n) {
  this.nSessid = n;
};

SiteTracker.prototype.setNUid = function(n) {
  this.nUid = n;
};

SiteTracker.prototype.setCst = function(n) {
  this.cst = n;
};

SiteTracker.prototype.setScreen = function(v) {
  this.screen = v;
};

SiteTracker.prototype.setCustomParam = function(v) {
  this.customParam = v;
}
SiteTracker.prototype.getParams = function(){
  return this.params;
}
