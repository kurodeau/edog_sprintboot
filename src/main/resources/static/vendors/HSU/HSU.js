
/*--------------------------------------------------------------
# 隱藏留言
--------------------------------------------------------------*/
  // var hideButtons = document.querySelectorAll(".hide-message");

  // hideButtons.forEach(function (button) {
  //   button.addEventListener("click", function () {
  //     var message = this.closest(".message-reply");
  //     message.classList.toggle("collapsed");
  //   });
  // });

  var toggleButtons = document.querySelectorAll(".toggle-message");

  toggleButtons.forEach(function (button) {
    button.addEventListener("click", function () {
      var message = this.closest(".message-reply");
      var isCollapsed = message.classList.contains("collapsed");

      if (isCollapsed) {
        // 如果留言是隱藏的，顯示留言
        message.classList.remove("collapsed");
        this.innerText = "隱藏留言";
      } else {
        // 如果留言是顯示的，隱藏留言
        message.classList.add("collapsed");
        this.innerText = "展開留言";
      }
    });
  });
  /*--------------------------------------------------------------
    # 頁籤
    --------------------------------------------------------------*/
  $("a.tab").on("click", function (e) {
    e.preventDefault();
    console.log("aaa");
    /* 將頁籤列表移除所有 -on，再將指定的加上 -on */
    $(this).closest("ul").find("a.tab").removeClass("-on");
    $(this).addClass("-on");

    /* 找到對應的頁籤內容，加上 -on 來顯示 */
    $("div.tab").removeClass("-on");
    $("div.tab." + $(this).attr("data-target")).addClass("-on");
  });
// document.querySelectorAll("a.tab").forEach(function(tab) {
//     tab.addEventListener("click", function(e) {
//       e.preventDefault();
//       console.log("aaa");
//       /* 將頁籤列表移除所有 -on，再將指定的加上 -on */
//       var tabs = this.closest("ul").querySelectorAll("a.tab");
//       tabs.forEach(function(tab) {
//         tab.classList.remove("-on");
//       });
//       this.classList.add("-on");
  
//       /* 找到對應的頁籤內容，加上 -on 來顯示 */
//       var target = this.getAttribute("data-target");
//       document.querySelectorAll("div.tab").forEach(function(divTab) {
//         divTab.classList.remove("-on");
//       });
//       document.querySelector("div.tab." + target).classList.add("-on");
//     });
//   });
  