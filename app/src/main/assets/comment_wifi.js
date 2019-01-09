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
      this._wrapOuter(imgList[i], "<div></div>")
    }
  }
}
LoadGifFn.prototype._wrapOuter = function(target, html) {
  var wrap = this._parseHTML(html);
  target.src = target.getAttribute('data-src');
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
var loadGifFn = new LoadGifFn(document.querySelector('.topic-description'))
loadGifFn.init();