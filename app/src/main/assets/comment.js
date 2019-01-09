var LoadGifFn = function(node) {
  if (node) {
    this.node = node;
  } else {
    console.log(node);
  }
}
LoadGifFn.prototype.init = function() {
  this._addLoadGifBtn();
}
LoadGifFn.prototype._addLoadGifBtn = function() {
  var imgList = this.node.querySelectorAll('img[data-src]');
  for (var i = 0, len = imgList.length; i < len; i++) {
    if (imgList[i].src.indexOf('.gif') !== -1 && imgList[i].src.indexOf('sqkt') !== -1) {
      this._wrapOuter(imgList[i], "<div class='git-wrap status0'></div>")
    }
  }
  this.node.addEventListener('click', this._loadGif)
}
LoadGifFn.prototype._wrapOuter = function(target, html) {
  var wrap = this._parseHTML(html);
  target.parentNode.insertBefore(wrap, target);
  target.previousSibling.appendChild(target)
}
LoadGifFn.prototype._parseHTML = function(str) {
  if (document.createRange) {
    var range = document.createRange()
    range.setStartAfter(document.body)
    return range.createContextualFragment(str)
  } else {
    return document.createElement(str)
  }
}
LoadGifFn.prototype._loadGif = function(ev) {

  ev = ev || window.event;
  var target = ev.target || ev.srcElement;
  if (target.nodeName.toLowerCase() === 'div' && target.classList.contains('status0')) {
    target.classList.remove('status0');
    target.classList.add('status1');
    var dataSrc = target.querySelector('img').getAttribute('data-src');
    var img = new Image();
    img.onload = function() {
      target.querySelector('img').src = dataSrc;
      target.classList.remove('status1');
    }
    img.onerror = function() {
      target.classList.remove('status1')
      target.classList.add('status0')
    }
    img.src = dataSrc;
  }
}
var loadGifFn = new LoadGifFn(document.querySelector('.topic-description'))
loadGifFn.init();