(function($) {
    "use strict";
    var $body = $("body"),
        $window = $(window),
        $bg_screen = $(".bg-screen"),
        $document = $(document);
    $window.on("load", function() {
        var $popup = $("#newslettermodal"),
            $preloader = $(".preloader");
        setTimeout(function () {
            $popup.delay(0).modal("show");
            $preloader.delay(0).fadeOut("fast");
        }, 100);
    });
    var WT = {
        init: function() {
            this.quickView();
            this.vega();
            this.vegatoggle();
            this.vegaMenu();
            this.dealSection();
            this.headerSticky();
            this.mobileMenu();
            this.searchField();
            this.cartDrawer();
            this.wishNote();
            this.qtyAdjust();
            this.cartqtyAdjust();
            this.filterDrawer();
            this.priceRange();
            this.productGrid();
            this.productOne();
            this.productTwo();
            this.productThree();
            this.productFour();
            this.productFive();
            this.productSix();
            this.productSeven();
            this.magnificPopup();
            this.productZoom();
            this.productReview();
            this.popularCategory();
            this.productRelated();
            this.productRelated2();
            this.readText();
            this.videoPopup();
            this.numberCounter();
            this.cartPage();
            this.checkOut();
            this.checkTab();
            this.checkOption();
            this.storeLocation();
            this.countDown();
            this.accountProfile();
            this.datePicker();
            this.addField();
            this.paginationNumber();
            this.resizeScreen();
            this.backTop();
        },
        quickView: function() {
            var galleryThumbs = new Swiper(".gallery-thumbs", {
                loop: false,
                spaceBetween: 15,
                slidesPerView: 4,
                thumbs: {
                    swiper: galleryTop,
                },
            });
            var galleryTop = new Swiper(".gallery-top", {
                loop: true,
                spaceBetween: 15,
                navigation: {
                    nextEl: ".quick-next",
                    prevEl: ".quick-prev",
                },
                thumbs: {
                    swiper: galleryThumbs,
                },
            });
        },
        vega: function() {
                $("a.browse-cat.browse-cat-lg").on("click", function() {
                $("#mobile-vega-menu").addClass('active');
                $(".bg-screen").addClass('active');
                $("body").addClass('hidden');
            });
            $(".vega-close button.vega-close-btn").on("click", function() {
                $("#mobile-vega-menu").removeClass('active');
                $(".bg-screen").removeClass('active');
                $("body").removeClass('hidden');
            });
        },
        vegatoggle: function() {
            var $toggler = $(".vegamenu-icon"),
                $menuvega_close = $(".vega-close button.menu-close-btn, .bg-screen"),
                $mobilevega_menu = $("#mobile-vega-menu");
            $toggler.on("click", function() {
                $mobilevega_menu.addClass("active");
            });
            $menuvega_close.on("click", function() {
                $mobilevega_menu.removeClass("active");
            });
        },
        vegaMenu: function() {
            var swiper = new Swiper('#vega-menu-slider', {
              slidesPerView: 1,
              grid: {
                rows: 1,
                fill: 'row' | 'column',
              },
              spaceBetween: 30,
              observer: true,
              observeParents: true,
              watchSlidesProgress: true,navigation: {
                nextEl: '.swiper-prev-vega',
                prevEl: '.swiper-next-vega',
              },
              pagination: {
                el: ".swiper-pagination-vega",
                clickable: true
              },
              autoplay: {
                delay: 5000,
                disableOnInteraction: false,
                pauseOnMouseEnter: true
              },
              speed: 1000,
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1299: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1399: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                }
            });
            var swiper = new Swiper('#vega-menu-slider1', {
              slidesPerView: 1,
              grid: {
                rows: 1,
                fill: 'row' | 'column',
              },
              spaceBetween: 30,
              observer: true,
              observeParents: true,
              watchSlidesProgress: true,navigation: {
                nextEl: '.swiper-prev-vega',
                prevEl: '.swiper-next-vega',
              },
              pagination: {
                el: ".swiper-pagination-vega",
                clickable: true
              },
              autoplay: {
                delay: 5000,
                disableOnInteraction: false,
                pauseOnMouseEnter: true
              },
              speed: 1000,
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1299: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1399: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                }
            });
            var swiper = new Swiper('#vega-menu-slidermobile', {
              slidesPerView: 1,
              grid: {
                rows: 1,
                fill: 'row' | 'column',
              },
              spaceBetween: 30,
              observer: true,
              observeParents: true,
              watchSlidesProgress: true,navigation: {
                nextEl: '.swiper-prev-vega',
                prevEl: '.swiper-next-vega',
              },
              pagination: {
                el: ".swiper-pagination-vega",
                clickable: true
              },
              autoplay: {
                delay: 5000,
                disableOnInteraction: false,
                pauseOnMouseEnter: true
              },
              speed: 1000,
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1299: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1399: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                }
            });
        },
        dealSection: function() {
            var deadline = new Date(Date.parse(new Date()) + 700 * 80 * 60 * 60 * 1000);
                initializeClock('clock1', deadline);
            var deadline = new Date(Date.parse(new Date()) + 650 * 80 * 60 * 60 * 1000);
                initializeClock('clock2', deadline);
            var deadline = new Date(Date.parse(new Date()) + 720 * 80 * 60 * 60 * 1000);
                initializeClock('clock3', deadline);
            var deadline = new Date(Date.parse(new Date()) + 420 * 80 * 60 * 60 * 1000);
                initializeClock('clock4', deadline);
            var deadline = new Date(Date.parse(new Date()) + 590 * 80 * 60 * 60 * 1000);
                initializeClock('clock5', deadline);
            function getTimeRemaining(endtime) {
                var t = Date.parse(endtime) - Date.parse(new Date());
                var seconds = Math.floor((t / 1000) % 60);
                var minutes = Math.floor((t / 1000 / 60) % 60);
                var hours = Math.floor((t / (1000 * 60 * 60)) % 24);
                var days = Math.floor(t / (1000 * 60 * 60 * 60));
                return {
                  'total': t,
                  'days': days,
                  'hours': hours,
                  'minutes': minutes,
                  'seconds': seconds
                };
            }
            function initializeClock(id, endtime) {
                var clock = document.getElementById(id);
                if(clock !== null){
                  var daysSpan = clock.querySelector('.days');
                  var hoursSpan = clock.querySelector('.hours');
                  var minutesSpan = clock.querySelector('.minutes');
                  var secondsSpan = clock.querySelector('.seconds');

                  function updateClock() {
                    var t = getTimeRemaining(endtime);
                    daysSpan.innerHTML = t.days;
                    hoursSpan.innerHTML = ('0' + t.hours).slice(-2);
                    minutesSpan.innerHTML = ('0' + t.minutes).slice(-2);
                    secondsSpan.innerHTML = ('0' + t.seconds).slice(-2);

                    if (t.total <= 0) {
                      clearInterval(timeinterval);
                    }
                  }

                  updateClock();
                  var timeinterval = setInterval(updateClock, 1000);
                }
            }
        },
        headerSticky: function() {
            $window.scroll(function() {
                var $header = $(".stickyheader"),
                    $sticky = $window.scrollTop();
                if($sticky >= 100) {
                    $header.addClass("sticky-header");
                } else {
                    $header.removeClass("sticky-header");
                }
            });
        },
        mobileMenu: function() {
            var $toggler = $(".vegamenu-icon button.toggler-btn, .toggler-wrapper button.toggler-btn"),
                $menu_close = $(".menu-close button.menu-close-btn, .bg-screen"),
                $mobile_menu = $("#mobile-menu");
            $toggler.on("click", function() {
                $mobile_menu.addClass("active");
                $bg_screen.addClass("active");
                $body.addClass("hidden");
            });
            $menu_close.on("click", function() {
                $mobile_menu.removeClass("active");
                $bg_screen.removeClass("active");
                $body.removeClass("hidden");
            });
        },
        searchField: function() {
            var $search_field = $("form.search-form .search-bar .form-search input.search-input");
            $search_field.each(function() {
                $search_field.on('keyup', function() {
                    var $search_object = $(this).val().toLowerCase(),
                        $search_input = $("form.search-form .search-bar .form-search input.search-input[type='search' i]"),
                        $search_btn = $(this).siblings("button"),
                        $search_result = $(this).parent().siblings(".search-results"),
                        $search_text = $(this).parent().siblings(".search-results").find("p.search-for").children(".search-text"),
                        $search_text_title = $(this).parents().siblings().find(".search-text"),
                        $search_more = $(this).parents().siblings().find("p.search-more"),
                        $search_fail = $(this).parents().siblings().find("p.search-fail");
                    if($search_object.length > 0) {
                        $search_btn.prop("disabled", false);
                        $search_result.addClass("active");
                    } else {
                        $search_btn.prop("disabled", true);
                        $search_result.removeClass("active");
                    }
                    $search_input.on("click", function() {
                        $search_btn.prop("disabled", true);
                        $search_result.removeClass("active");
                    });
                    $search_text.text($search_object);
                    $search_text_title.text($search_object);
                    $(".search-results ul li").filter(function() {
                        $(this).toggle($(this).text().toLowerCase().indexOf($search_object) > -1);
                        if($(this).toggle($(this).text().toLowerCase().indexOf($search_object) > -1)) {
                            $search_more.show();
                            $search_fail.hide();
                        } else {
                            $search_more.hide();
                            $search_fail.show();
                        }
                    });
                });
            });
        },
        cartDrawer: function() {
            var $cart_menu = $(".cart-wrapper a.js-cart-drawer, .bottom-menu-wrapper a.bottom-menu-cart"),
                $drawer_close = $(".drawer-close button.drawer-close-btn, .bg-screen"),
                $cart_drawer = $("#cart-drawer");
            $cart_menu.on("click", function() {
                miniCart();
            });
            $drawer_close.on("click", function() {
                $cart_drawer.removeClass("active");
                $bg_screen.removeClass("active");
                $body.removeClass("hidden");
            });
            var $add_to_cart = $(".add-to-cart");
            $add_to_cart.on("click", function(event) {
                event.preventDefault();
                var $this = $(this);
                $this.addClass("loading active disabled");
                setTimeout(function() {
                    $this.removeClass("loading");
                    $this.addClass("done");
                    setTimeout(function() {
                        $this.removeClass("done active disabled");
                        miniCart();
                        $this.parents(".quickview-modal").find(".quickview-modal-close").trigger('click');
                    }, 500);
                }, 500);
            });
            function miniCart() {
                $cart_drawer.addClass("active");
                $bg_screen.addClass("active");
                $body.addClass("hidden");
            }
        },
        wishNote: function() {
            var $wish_note_content = $(".wish-note-content");
            $wish_note_content.each(function() {
                var $wish_btn_edit = $("form button.wish-edit"),
                    $wish_textarea_focus = $("form .wish-textarea textarea[name]"),
                    $wish_btn_save = $("form button.wish-save"),
                    $wish_btn_cancel = $("form button.wish-cancel");
                $wish_btn_edit.on("click", function() {
                    var $check_wish_btn = $(this).parent();
                    if($check_wish_btn.hasClass("true")) {
                        $check_wish_btn.removeClass("true");
                    } else {
                        $check_wish_btn.addClass("false");
                    }
                    var $wish_textarea = $(this).parent().siblings(".wish-textarea");
                    if($wish_textarea.hasClass("false")) {
                        $wish_textarea.removeClass("false");
                    } else {
                        $wish_textarea.addClass("true");
                    }
                });
                $wish_textarea_focus.on("click", function() {
                    $(this).addClass("focus");
                    var $wish_btn = $(this).parents(".wish-textarea").siblings(".wish-btn");
                    if($wish_btn.hasClass("false")) {
                        $wish_btn.removeClass("false");
                    } else {
                        $wish_btn.addClass("true");
                    }
                });
                $wish_btn_save.on("click", function() {
                    $(this).parents(".wish-btn").siblings(".wish-textarea").find("textarea[name]").removeClass("focus");
                    var $wish_btn = $(this).parents(".wish-btn");
                    if($wish_btn.hasClass("true")) {
                        $wish_btn.removeClass("true");
                    } else {
                        $wish_btn.addClass("false");
                    }
                });
                $wish_btn_cancel.on("click", function() {
                    $(this).parents(".wish-btn").siblings(".wish-textarea").find("textarea[name]").removeClass("focus");
                    var $wish_btn = $(this).parents(".wish-btn");
                    if($wish_btn.hasClass("true")) {
                        $wish_btn.removeClass("true");
                    } else {
                        $wish_btn.addClass("false");
                    }
                });
            });
            var $wish_list = $(".add-to-wishlist");
            $wish_list.on("click", function() {
                var $this = $(this);
                $this.addClass("active disabled");
                setTimeout(function() {
                    $this.find("i").removeClass("ri-heart-line").addClass("ri-heart-fill");
                    $this.removeClass("active disabled");
                    $this.attr("href", "wishlist.html");
                }, 500);
            });
        },
        cartqtyAdjust: function() {
            $('.dec').on("click",function () {
                var $input = $(this).parent().find('input');
                var count = parseInt($input.val()) - 1;
                count = parseInt(count) < 1 ? 1 : parseInt(count);
                $input.val(parseInt(count));
                $input.trigger("change");
                return false;
            });
            $('.inc').on("click",function () {
                var $input = $(this).parent().find('input');
                $input.val(parseInt($input.val()) + 1);
                $input.trigger("change");
                return false;
            });
        },
        qtyAdjust: function() {
            var $qty_minus = $("button.js-qty-adjust-minus"),
                $qty_plus = $("button.js-qty-adjust-plus");
            $qty_minus.on("click", function() {
                var $input = $(this).parent().find("input.js-qty-num"),
                    $count = parseInt($input.val()) - 1;
                $count = parseInt($count) < 1 ? 1 : parseInt($count);
                $input.val($count);
                $input.trigger("change");
                return false;
            });
            $qty_plus.on("click", function() {
                var $input = $(this).parent().find("input.js-qty-num");
                $input.val(parseInt($input.val()) + 1);
                $input.trigger("change");
                return false;
            });
        },
        filterDrawer: function() {
            var $filter_btn = $("button.filter-button"),
                $filter_close = $("button.close-filter-sidebar, .bg-screen"),
                $filter_sidebar = $(".filter-sidebar");
            $filter_btn.on("click", function() {
                $filter_sidebar.addClass("active");
                $bg_screen.addClass("active");
                $body.addClass("hidden");
            });
            $filter_close.on("click", function() {
                $filter_sidebar.removeClass("active");
                $bg_screen.removeClass("active");
                $body.removeClass("hidden");
            });

            
            $('button.filter-button').on('click', function () {
            $('.filter-sidebar').addClass("active");
            $('.bg-screen').addClass("active");
            });
            $('button.close-sidebar').on('click', function () {
                $('.filter-sidebar').removeClass("active");
                $('.bg-screen ').removeClass("active");
            });
        },
        priceRange: function() {
                if($('#range1').length){
                var slider1 = document.getElementById("range1");
                var slider2 = document.getElementById("range2");
                var output1 = document.getElementById("demo1");
                var output2 = document.getElementById("demo2");
                output1.innerHTML = slider1.value;
                output2.innerHTML = slider2.value;

                slider1.oninput = function() {
                    output1.innerHTML = slider1.value;
                }
                slider2.oninput = function() {
                    output2.innerHTML = slider2.value;
                }
            }
        },
        productGrid: function() {
            $('.list-change-view').on("click",function () {
                event.preventDefault();
                var data_grid = $(this).attr('data-grid-view');
                if ($('.special-product').hasClass('grid-1') || 
                    $('.special-product').hasClass('grid-2') || 
                    $('.special-product').hasClass('grid-3') || 
                    $('.special-product').hasClass('grid-4')) 
                {
                    $('.special-product').removeClass('grid-1');
                    $('.special-product').removeClass('grid-2');
                    $('.special-product').removeClass('grid-3');
                    $('.special-product').removeClass('grid-4');
                    $('.special-product').addClass('grid-'+data_grid);
                }
                else {
                    $('.special-product').addClass('grid-'+data_grid);
                }
            });
            $('.list-change-view').on("click",function () {
                $('.list-change-view').removeClass('active');
                $(this).addClass('active');
            });
        },
        productOne: function() {
            var $slider_big = $(".slider-big"),
                $slider_small = $(".slider-small");
            $slider_big.slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: false,
                centerMode: true,
                centerPadding: '0px',
                asNavFor: '.slider-small',
                adaptiveHeight: true
            });
            $slider_small.slick({
                slidesToShow: 4,
                slidesToScroll: 1,
                arrows: true,
                prevArrow: '<button class="slick-prev"><i class="feather-arrow-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="feather-arrow-right"></i></button>',
                dots: false,
                centerMode: true,
                centerPadding: '0px',
                focusOnSelect: true,
                asNavFor: '.slider-big',
                responsive: [
                {
                    breakpoint: 480,
                    settings: {
                    slidesToShow: 3
                    }
                }
                ]
            });
        },
        productTwo: function() {
            $('.style2-slider-big').slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: false,
                centerMode: true,
                centerPadding: '0px',
                asNavFor: '.style2-slider-small',
                adaptiveHeight: true,
            });
            $('.style2-slider-small').slick({
                slidesToShow: 3,
                slidesToScroll: 1,
                arrows: true,
                prevArrow: '<button class="slick-prev"><i class="bi bi-chevron-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="bi bi-chevron-right"></i></button>',
                dots: false,
                centerMode: true,
                centerPadding: '0px',
                focusOnSelect: true,
                asNavFor: '.style2-slider-big',
                vertical: true,
                verticalSwiping: true,
                responsive: [
                    {
                        breakpoint: 480,
                        settings: {
                          vertical: false,
                          verticalSwiping: false
                        }
                    }
                ]
            });
        },
        productThree: function() {
            var $slider_small3 = $(".style3-slider-small");
            $slider_small3.slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: true,
                prevArrow: '<button class="slick-prev"><i class="feather-arrow-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="feather-arrow-right"></i></button>',
                dots: false,
                centerMode: true,
                centerPadding: 'calc(33.33% - 30px)',
                focusOnSelect: true,
                responsive: [
                {
                    breakpoint: 480,
                    settings: {
                        arrows: false,
                        dots: true,
                        centerMode: true,
                        centerPadding: '0px',
                    }
                },
                {
                    breakpoint: 640,
                    settings: {
                        arrows: false,
                        dots: true,
                        centerMode: true,
                        centerPadding: '0px',
                    }
                },
                {
                    breakpoint: 540,
                    settings: {
                        arrows: false,
                        dots: true,
                        centerMode: true,
                        centerPadding: '0px',
                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        arrows: false,
                        dots: true,
                        centerMode: true,
                        centerPadding: 'calc(33.33% - 30px)',
                    }
                },
                {
                    breakpoint: 1024,
                    settings: {
                        arrows: false,
                        dots: true,
                        centerMode: true,
                        centerPadding: 'calc(33.33% - 30px)',
                    }
                },
                {
                    breakpoint: 1199,
                    settings: {
                        arrows: false,
                        dots: true,
                        centerMode: true,
                        centerPadding: 'calc(33.33% - 30px)',
                    }
                }
                ]
            });
        },
        productFour: function() {
            $('.style4-slider-big').slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: false,
                centerMode: true,
                centerPadding: '0px',
                asNavFor: '.style4-slider-small',
                adaptiveHeight: true,
            });
            $('.style4-slider-small').slick({
                slidesToShow: 4,
                slidesToScroll: 1,
                arrows: true,
                prevArrow: '<button class="slick-prev"><i class="bi bi-chevron-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="bi bi-chevron-right"></i></button>',
                dots: false,
                centerMode: true,
                centerPadding: '0px',
                focusOnSelect: true,
                asNavFor: '.style4-slider-big',
                vertical: true,
                verticalSwiping: true,
                responsive: [
                    {
                        breakpoint: 480,
                        settings: {
                          vertical: false,
                          verticalSwiping: false
                        }
                    }
                ]
            });
        },
        productFive: function() {
            var $slider_small5 = $(".style5-slider-small");
            $slider_small5.slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: false,
                prevArrow: '<button class="slick-prev"><i class="feather-arrow-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="feather-arrow-right"></i></button>',
                dots: true,
                focusOnSelect: true
            });
        },
        productSix: function() {
            $('.slider-big-6').slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: false,
                centerMode: true,
                centerPadding: '0px',
                asNavFor: '.slider-small-6',
                adaptiveHeight: true,
            });
            $('.slider-small-6').slick({
                slidesToShow: 4,
                slidesToScroll: 1,
                arrows: true,
                prevArrow: '<button class="slick-prev"><i class="bi bi-chevron-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="bi bi-chevron-right"></i></button>',
                dots: false,
                centerMode: true,
                centerPadding: '0px',
                focusOnSelect: true,
                asNavFor: '.slider-big-6',
                responsive: [{
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 3
                    }
                }]
            });
        },
        productSeven: function() {
            $('.slider-big-7').slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: false,
                centerMode: true,
                centerPadding: '0px',
                asNavFor: '.slider-small-7',
                adaptiveHeight: true,
            });
            $('.slider-small-7').slick({
                slidesToShow: 4,
                slidesToScroll: 1,
                arrows: false,
                prevArrow: '<button class="slick-prev"><i class="bi bi-chevron-left"></i></button>',
                nextArrow: '<button class="slick-next"><i class="bi bi-chevron-right"></i></button>',
                dots: false,
                centerMode: true,
                centerPadding: '0px',
                focusOnSelect: true,
                asNavFor: '.slider-big-7',
                    responsive: [{
                    breakpoint: 480,
                        settings: {
                        slidesToShow: 3
                    }
                }]
            });
        },
        magnificPopup: function() {
            $('.full-view').on('click', function () {
                $(this).next().magnificPopup('open');
            });
            $('.slider-big, .style2-slider-big, .style4-slider-big, .slider-big-6, .slider-big-7').magnificPopup({
                delegate: 'a',
                type: 'image',
                showCloseBtn: true,
                closeBtnInside: false,
                midClick: true,
                tLoading: 'Loading image #%curr%...',
                mainClass: 'mfp-img-mobile',
                gallery: {
                    enabled: true,
                    navigateByImgClick: true,
                preload: [0,1] // Will preload 0 - before current, and 1 after the current image
                },
                image: {
                    tError: '<a href="%url%">The image #%curr%</a> could not be loaded.'
                }
            });
        },
        productZoom: function() {
            var $pro_zoom = $("img.zoom");
            $pro_zoom.wrap('<span class="pro-zoom" style="display:block"></span>').css('display', 'block').parent().zoom({
                url: $(this).find('img').attr('data-zoom')
            });
        },
        productReview: function() {
            var $pro_review_btn = $("button#pro-review-btn");
            $pro_review_btn.on("click", function () {
                var $pro_review_form = $(this).parents().siblings("#pro-review-form");
                if($pro_review_form.hasClass("active")) {
                    $pro_review_form.removeClass("active");
                } else {
                    $pro_review_form.addClass("active");
                }
            });
        },
        popularCategory: function() {
            var swiper = new Swiper('#slider-category', {
              grid: {
                rows: 1,
                fill: 'row' | 'column',
              },
              spaceBetween: 30,
              observer: true,
              observeParents: true,
              watchSlidesProgress: true,
              navigation: {
                nextEl: '.swiper-category-prev',
                prevEl: '.swiper-category-next',
              },
              pagination: {
                el: ".swiper-pagination-category",
                clickable: true
              },
               autoplay: {
              delay: 5000,
              disableOnInteraction: false,
              pauseOnMouseEnter: true
              },
              speed: 1000,
              breakpoints: {
                0: {
                  slidesPerView: 1,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 12
                },
                320: {
                  slidesPerView: 1,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 12
                },
                360: {
                  slidesPerView: 2,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 12
                },
                540: {
                  slidesPerView: 3,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 12
                },
                640: {
                  slidesPerView: 4,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 12
                },
                768: {
                  slidesPerView: 2,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 30
                },
                1024: {
                  slidesPerView: 3,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 30
                },
                1199: {
                  slidesPerView: 4,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 30
                },
                1299: {
                  slidesPerView: 4,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 30
                },
                1399: {
                  slidesPerView: 5,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 30
                },
                1499: {
                  slidesPerView: 5,
                  grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                  },
                  spaceBetween: 30
                }
              }
            });
        },
        productRelated: function() {
            var swiper = new Swiper(".swiper#related-slider", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    nextEl: '.swiper-next-related',
                    prevEl: '.swiper-prev-related',
                },
                pagination: {
                    el: ".swiper-pagination-related",
                    clickable: true,
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
        productRelated2: function() {
            var swiper = new Swiper(".swiper#related-slider-full", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    nextEl: '.swiper-next-related',
                    prevEl: '.swiper-prev-related',
                },
                pagination: {
                    el: ".swiper-pagination-related",
                    clickable: true,
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1499: {
                        slidesPerView: 5,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
        readText: function() {
            var $read_txt = $("label.login-read-text");
            $read_txt.on("click", function() {
                var $login_checkbox = $(this).children(".login-checkbox").is(':checked'),
                    $register_btn = $(this).siblings("button.register-btn");
                if($login_checkbox) {
                    $register_btn.removeClass("disabled");
                }
                else {
                    $register_btn.addClass("disabled");
                }
            });
        },
        videoPopup: function() {
            var $video_popup = $(".play-button");
            $video_popup.magnificPopup ({
                tClose: 'Close (Esc)',
                type: 'iframe',
                mainClass: 'mfp-fade',
                removalDelay: 160,
                preloader: false,
                fixedContentPos: false
            });
        },
        numberCounter: function() {
            $window.scroll(function() {
                var $count_number = $(".count-number");
                $count_number.each(function() {
                    var $this = $(this),
                        countTo = $this.attr('data-count');
                    $({countNum: $this.text()}).animate({
                        countNum: countTo
                    },
                    {
                        duration: 3000,
                        easing: 'linear',
                        step: function() {
                            $this.text(Math.floor(this.countNum));
                        },
                        complete: function() {
                            $this.text(this.countNum);
                        }
                    });
                });
            });
        },
        cartPage: function() {
            var $wt_discount_info = $(".cp-discount-info");
            $wt_discount_info.each(function() {
                var $form_btn_edit = $("form button.cp-dc-edit"),
                    $form_btn_close = $("form button.cp-dc-close"),
                    $form_btn_apply = $("form button.cp-dc-apply");
                $form_btn_edit.on("click", function() {
                    var $wt_edit_btn = $(this);
                    if($wt_edit_btn.hasClass("true")) {
                        $(this).removeClass("true").addClass("false");
                        $($wt_edit_btn).siblings().removeClass("false").addClass("true");
                        $($wt_edit_btn).parents(".cp-discount-wrap").siblings(".cp-discount-coupan").removeClass("true").addClass("false");
                        $($wt_edit_btn).parents(".cp-discount-wrap").siblings(".cp-discount-detail").removeClass("false").addClass("true");
                    } else {
                        $(this).addClass("true").removeClass("false");
                        $($wt_edit_btn).siblings().addClass("false").removeClass("true");
                        $($wt_edit_btn).parents(".cp-discount-wrap").siblings(".cp-discount-coupan").addClass("false").removeClass("true");
                        $($wt_edit_btn).parents(".cp-discount-wrap").siblings(".cp-discount-detail").addClass("false").removeClass("true");
                    }
                });
                $form_btn_close.on("click", function() {
                    var $wt_close_btn = $(this);
                    if($wt_close_btn.hasClass("true")) {
                        $(this).removeClass("true").addClass("false");
                        $($wt_close_btn).siblings().removeClass("false").addClass("true");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-coupan").removeClass("false").addClass("true");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-detail").removeClass("true").addClass("false");
                    } else {
                        $(this).addClass("true").removeClass("false");
                        $($wt_close_btn).siblings().addClass("false").removeClass("true");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-coupan").addClass("true").removeClass("false");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-detail").addClass("true").removeClass("false");
                    }
                });
                $form_btn_apply.on("click", function() {
                    var $wt_close_btn = $(this);
                    if($wt_close_btn.hasClass("true")) {
                        $(this).removeClass("true").addClass("false");
                        $($wt_close_btn).siblings().removeClass("false").addClass("true");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-coupan").removeClass("false").addClass("true");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-detail").removeClass("true").addClass("false");
                    } else {
                        $(this).addClass("true").removeClass("false");
                        $($wt_close_btn).siblings().addClass("false").removeClass("true");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-coupan").addClass("true").removeClass("false");
                        $($wt_close_btn).parents(".cp-discount-wrap").siblings(".cp-discount-detail").addClass("true").removeClass("false");
                    }
                });
            });
            // cart-slider
            var swiper = new Swiper(".swiper#cart-slider", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    nextEl: '.swiper-next-related',
                    prevEl: '.swiper-prev-related',
                },
                pagination: {
                    el: ".swiper-pagination-related",
                    clickable: true,
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
        checkOut: function() {
            var $checkout_info = $(".checkout-info");
            $checkout_info.each(function() {
                var $form_btn_edit = $("form button.check-edit"),
                    $form_btn_save = $("form button.check-save");
                $form_btn_edit.on("click", function() {
                    var $check_edit_btn = $(this);
                    if($check_edit_btn.hasClass("true")) {
                        $(this).removeClass("true");
                    } else {
                        $(this).addClass("false");
                    }
                    var $check_detail_done = $($check_edit_btn).parent().siblings(".checkout-detail-done");
                    if($check_detail_done.hasClass("true")) {
                        $check_detail_done.removeClass("true");
                    } else {
                        $check_detail_done.addClass("false");
                    }
                    var $check_detail = $($check_edit_btn).parent().siblings(".checkout-detail");
                    if($check_detail.hasClass("false")) {
                        $check_detail.removeClass("false");
                    } else {
                        $check_detail.addClass("true");
                    }
                    var $check_sc = $($check_edit_btn).parent().siblings(".check-sc");
                    if($check_sc.hasClass("false")) {
                        $check_sc.removeClass("false");
                    } else {
                        $check_sc.addClass("true");
                    }
                    var $check_save_btn = $($check_edit_btn).parent().siblings(".check-sc").find("button.check-save");
                    if($check_save_btn.hasClass("false")) {
                        $check_save_btn.removeClass("false");
                    } else {
                        $check_save_btn.addClass("true");
                    }
                });
                $form_btn_save.on("click", function() {
                    var $check_save_btn = $(this);
                    if($check_save_btn.hasClass("true")) {
                        $(this).removeClass("true");
                    } else {
                        $(this).addClass("false");
                    }
                    var $check_edit_btn = $($check_save_btn).parents(".check-sc").siblings(".checkout-title").children("button.check-edit");
                    if($check_edit_btn.hasClass("false")) {
                        $check_edit_btn.removeClass("false");
                    } else {
                        $check_edit_btn.addClass("true");
                    }
                    var $check_detail_done = $($check_save_btn).parents(".check-sc").siblings(".checkout-detail-done");
                    if($check_detail_done.hasClass("false")) {
                        $check_detail_done.removeClass("false");
                    } else {
                        $check_detail_done.addClass("true");
                    }
                    var $check_detail = $($check_save_btn).parents(".check-sc").siblings(".checkout-detail");
                    if($check_detail.hasClass("true")) {
                        $check_detail.removeClass("true");
                    } else {
                        $check_detail.addClass("false");
                    }
                    var $check_sc = $($check_save_btn).parents(".check-sc");
                    if($check_sc.hasClass("true")) {
                        $check_sc.removeClass("true");
                    } else {
                        $check_sc.addClass("false");
                    }
                });
            });
        },
        checkTab: function() {
            var $checktab_content = $(".checktab-content");
            $checktab_content.each(function() {
                var $tab_progress = $("#checktab-progress"),
                    $tab_next_btn = $("button.checktab-next"),
                    $tab_back_btn = $("button.checktab-back"),
                    $tab_step = $(".checktab-content .checktab-step"),
                    $tab_li = $(".checkout-tab ul.checktab-ul li.checktab-li");
                var $tab_active = 1;
                $tab_next_btn.on("click", function() {
                    if($tab_step.hasClass("active")) {
                        $tab_step.removeClass("active");
                        $(this).parents().next(".checktab-content .checktab-step").addClass("active");
                    }
                    if($tab_li.hasClass("active")) {
                        $(this).parents().siblings(".checkout-tab").find("li.checktab-li.active").removeClass("active").next("li.checktab-li").addClass("active").prev().addClass("done");
                    }
                    if ($tab_active < $tab_step.length) {
                        $tab_active++;
                    }
                    tabUpdate();
                });
                $tab_back_btn.on("click", function() {
                    $tab_step.removeClass("active");
                    $(this).parents().prev(".checktab-content .checktab-step").addClass("active");
                    $(this).parents().siblings(".checkout-tab").find("li.checktab-li.active").removeClass("active").prev("li.checktab-li").addClass("active").removeClass("done");
                    if ($tab_active > 1) {
                        $tab_active--;
                    }
                    tabUpdate();
                });
                function tabUpdate() {
                    $tab_progress.css("width", (($tab_active - 1) / ($tab_step.length - 1)) * 100 + "%");
                }
            });
        },
        checkOption: function() {
            // checkbox
            var $check_add = $(".check-aw");
            $check_add.each(function() {
                var $check_box = $(".checkadd-box label.cust-checkbox-label input[name]");
                $check_box.on("click", function() {
                    var $check_bill_add = $(this).parent().siblings(".check-billing");
                    $check_box.not(this).prop("checked", false);
                    if($check_box.not(this).prop("checked", false)) {
                        $(".check-billing").removeClass("hide");
                        $check_bill_add.addClass("hide");
                    }
                    if($(this).prop("checked") == true) {
                        $check_bill_add.addClass("hide");
                    }
                    if($(this).prop("checked") == false) {
                        $check_bill_add.removeClass("hide");
                    }
                });
            });
            // method-radio
            var $check_meth_radio = $(".check-method label.cust-radio-label input[name]");
            $check_meth_radio.each(function() {
                $check_meth_radio.on("click", function() {
                    var $check_radio_checked = $(this).is(':checked'),
                        $check_method = $(".check-method-option"),
                        $check_method_false = $(this).attr("checked", "false").parents(".check-lsdt").siblings(".check-method-option"),
                        $check_method_true = $(this).attr("checked", "true").parents(".check-lsdt").siblings(".check-method-option");
                    if($check_radio_checked) {
                        if($check_method.hasClass("active")) {
                            $check_method.removeClass("active");
                            $check_method_false.addClass("active");
                        } else {
                            $check_method.addClass("active");
                            $check_method_true.removeClass("active");
                        }
                    }
                });
            });
            // payment-radio
            var $check_pay_radio = $(".check-payment .check-ps label.cust-radio-label input[name]");
            $check_pay_radio.each(function() {
                $check_pay_radio.on("click", function() {
                    var $check_radio_checked = $(this).is(':checked'),
                        $check_pay = $(".check-pay-option"),
                        $check_pay_false = $(this).attr("checked", "false").parents(".check-ps").siblings(".check-pay-option"),
                        $check_pay_true = $(this).attr("checked", "true").parents(".check-ps").siblings(".check-pay-option");
                    if($check_radio_checked) {
                        if($check_pay.hasClass("active")) {
                            $check_pay.removeClass("active");
                            $check_pay_false.addClass("active");
                        } else {
                            $check_pay.addClass("active");
                            $check_pay_true.removeClass("active");
                        }
                    }
                });
            });
            // card-payment
            var $checkout_form = $(".checkout-form");
            $checkout_form.card({
                container: '.card-wrapper',
                formatting: true,
                formSelectors: {
                    numberInput: 'input[name="cardnumber"]',
                    expiryInput: 'input[name="exp-date"]',
                    cvcInput: 'input[name="cvc"]',
                    nameInput: 'input[name="ccname"]'
                },
                cardSelectors: {
                    cardContainer: '.jp-card-container',
                    card: '.jp-card',
                    numberDisplay: '.jp-card-number',
                    expiryDisplay: '.jp-card-expiry',
                    cvcDisplay: '.jp-card-cvc',
                    nameDisplay: '.jp-card-name'
                },
                messages: {
                    validDate: 'valid\nthru',
                    monthYear: 'mm / yy'
                },
                placeholders: {
                    number: '&bull;&bull;&bull;&bull; &bull;&bull;&bull;&bull; &bull;&bull;&bull;&bull; &bull;&bull;&bull;&bull;',
                    cvc: '&bull;&bull;&bull;',
                    expiry: '&bull;&bull;/&bull;&bull;',
                    name: 'Full name'
                },
                masks: {
                    cardNumber: false
                },
                classes: {
                    valid: 'jp-card-valid',
                    invalid: 'jp-card-invalid'
                },
                debug: false
            });
        },
        storeLocation: function() {
            $('form.store-form #store-btn').on("click", function() {
                var $error = 'false';
                $('form.store-form .store-search .store-select select').each(function() {
                    var $city = $('select#city').val(); //find from class
                    var $outlet = $('select#outlet').val(); //find to class

                    // Check if "to" is empty and "from" is selected
                    if($city != "" && $outlet == "") {
                        $error = 'true';
                        return 0;
                    }
                    // Check if "from" is empty and "to" is selected
                    if($outlet != "" && $city == "") {
                        $error = 'true';
                        return 0;
                    }
                });
                if($error == 'true') {
                    setTimeout(function () { 
                        $('#alert-msg').html('<div class="alert alert-danger">Please Select Both Filed!</div>'); 
                    }, 0);
                } else {
                    $('#store-location').addClass('active');
                }

                $("#city").change(function() {
                if ($(this).data('options') === undefined) {
                /*Taking an array of all options-2 and kind of embedding it on the select1*/
                $(this).data('options', $('#outlet option').clone());
                }
                var id = $(this).val();
                var options = $(this).data('options').filter('[value=' + id + ']');
                $('#outlet').html(options);
            });
            $("#city").change(function() {
                $("#citySelect").val($("#city option:selected").text());
            });
            $("#outlet").change(function() {
                $("#outletSelect").val($("#outlet option:selected").text());
            });
            });
        },
        countDown: function() {
            var $count_down = $(".countdown");
            $count_down.each(function() {
                var $timer = $(this).attr("data-time"),
                    $countDownDate = new Date($timer).getTime(),
                    $this = $(this);
                var $x = setInterval(function() {
                    var $now = new Date().getTime(),
                        $distance = $countDownDate - $now,
                        $days = Math.floor($distance / (1000 * 60 * 60 * 24)),
                        $hours = Math.floor(($distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
                        $minutes = Math.floor(($distance % (1000 * 60 * 60)) / (1000 * 60)),
                        $seconds = Math.floor(($distance % (1000 * 60)) / 1000);
                        $this.find('.day').html($days);
                        $this.find('.hrs').html($hours);
                        $this.find('.min').html($minutes);
                        $this.find('.sec').html($seconds);
                        // $this.text($days + "d " + $hours + "h " +  $minutes + "m " + $seconds + "s ");
                    if($distance < 0) {
                        clearInterval($x);
                        $this.find('.day').html('0');
                        $this.find('.hrs').html('0');
                        $this.find('.min').html('0');
                        $this.find('.sec').html('0');
                        // $this.text("EXPIRED");
                    }
                }, 1000);
            });
        },
        accountProfile: function() {
            var $ap_di = $(".ap-di");
            $ap_di.each(function() {
                var $form_btn_edit = $("form button.ap-edit"),
                    $form_btn_save = $("form button.ap-save"),
                    $form_btn_cancel = $("form button.ap-cancel");
                $form_btn_edit.on("click", function() {
                    var $ap_edit_btn = $(this).hide();
                    $($ap_edit_btn).parent().siblings(".ap-ddone").hide();
                    $($ap_edit_btn).parent().siblings(".ap-ddetail").show();
                    $($ap_edit_btn).parent().siblings(".ap-sc").show();
                    $($ap_edit_btn).parent().siblings(".ap-sc").find(".ap-btn").find("button.ap-save, button.ap-cancel").show();
                });
                $form_btn_save.on("click", function() {
                    var $ap_save_btn = $(this).hide();
                    $($ap_save_btn).parents().siblings(".ap-dtitle").children("button.ap-edit").show();
                    $($ap_save_btn).parents().siblings(".ap-ddone").show();
                    $($ap_save_btn).parents().siblings(".ap-ddetail").hide();
                    $($ap_save_btn).parents(".ap-di").children(".ap-sc").hide();
                });
                $form_btn_cancel.on("click", function() {
                    var $ap_cancel_btn = $(this).hide();
                    $($ap_cancel_btn).parents().siblings(".ap-dtitle").children("button.ap-edit").show();
                    $($ap_cancel_btn).parents().siblings(".ap-ddone").show();
                    $($ap_cancel_btn).parents().siblings(".ap-ddetail").hide();
                    $($ap_cancel_btn).parents(".ap-di").children(".ap-sc").hide();
                });
            });
        },
        datePicker: function() {
            var $date = $("#date");
            $date.each(function() {
                var $date_id = this.id;
                $("input[id=" + $date_id + "]").duDatepicker({
                    format: 'dd.mm.yyyy',
                    outFormat: 'null',
                    theme: 'null',
                    auto: true,
                    inline: false,
                    clearBtn: false,
                    cancelBtn: false,
                    overlayClose: true,
                    maxDate: 'today'
                });
                $("input[id=" + $date_id + "]").duDatepicker('setValue', '14.05.1991');
            });
        },
        addField: function() {
            var $ap_dc = $(".ap-dc"),
                $max_input_field = 10,
                $dup_input_field = $(".wt-input-field"),
                $x = 1,
                $i = 0;
            // email
            var $add_email_btn = $(".ap-add-email");
            $add_email_btn.on("click", function() {
                if($x < $max_input_field || $i < $max_input_field) {
                    $x++;
                    var $dup_email_html = '<div class="ap-dcadd-box wt-mail-box"><h6>Add a new email</h6><label class="cust-checkbox-label"><span>Set as default mail</span><input type="checkbox" id="default-email-' + $i + '" name="default-email-' + $i + '" class="cust-checkbox"><span class="cust-check"></span></label><div class="ap-dc-field ap-dc-mail"><ul class="field-ul"><li class="field-li field-half"><label for="email-' + $i + '" class="field-label">Email</label><input type="email" id="email-' + $i + '" name="email" placeholder="Email" autocomplete="email"></li></ul></div><div class="ap-remove-field"><button type="button" class="wt-field-btn ap-remove-email"><span class="wt-field-icon"><i class="feather-minus"></i></span><span class="wt-field-text">Remove email</span></button></div></div>';
                    $(this).parent().siblings(".ap-dc").append($dup_email_html);
                    $i++;
                }
            });
            $dup_input_field.on("click", ".ap-remove-email", function(e) {
                e.preventDefault();
                $(this).parents(".ap-dcadd-box").remove();
                $x--;
            });
            $ap_dc.on("click", ".wt-mail-box label.cust-checkbox-label input[name]", function() {
                var $ap_box = $(".wt-mail-box label.cust-checkbox-label input[name]"),
                    $ap_dc_mail = $(this).parent().siblings(".ap-dc-mail");
                $ap_box.not(this).prop("checked", false);
                if($ap_box.not(this).prop("checked", false)) {
                    $(".ap-dc-mail").removeClass("hide");
                    $ap_dc_mail.addClass("hide");
                }
                if($(this).prop("checked") == true) {
                    $ap_dc_mail.addClass("hide");
                }
                if($(this).prop("checked") == false) {
                    $ap_dc_mail.removeClass("hide");
                }
            });
            // shipping address
            var $add_ship_btn = $(".ap-add-ship");
            $add_ship_btn.on("click", function() {
                if($x < $max_input_field || $i < $max_input_field) {
                    $x++;
                    var $dup_ship_html = '<div class="ap-dcadd-box wt-ship-box"><h6>Add a new address</h6><label class="cust-checkbox-label"><span>Set as default address</span><input type="checkbox" id="default-address-' + $i + '" name="default-address-' + $i + '" class="cust-checkbox"><span class="cust-check"></span></label><div class="ap-dc-field ap-dc-ship"><ul class="field-ul"><li class="field-li field-half"><label for="address-' + $i + '" class="field-label">Address</label><input type="text" id="address-' + $i + '" name="address" placeholder="Address" autocomplete="address-line1"></li><li class="field-li field-half"><label for="house-no-' + $i + '" class="field-label">Apartment, suite, etc.</label><input type="text" id="house-no-' + $i + '" name="address" placeholder="Apartment, suite, etc." autocomplete="address-line2"></li><li class="field-li field-half"><label for="country-' + $i + '" class="field-label">Country</label><select id="country-' + $i + '" name="country" autocomplete="country"><option selected="selected" disabled="disabled">--Select your country--</option><option>India</option><option>Uk</option><option>Usa</option><option>Uae</option><option>Bangladesh</option></select></li><li class="field-li field-half"><label for="province-' + $i + '" class="field-label">State</label><input type="text" id="province-' + $i + '" name="province" placeholder="State" autocomplete="address-level1"></li><li class="field-li field-half"><label for="city-' + $i + '" class="field-label">City</label><input type="text" id="city-' + $i + '" name="city" placeholder="City" autocomplete="address-level2"></li><li class="field-li field-half"><label for="pincode-' + $i + '" class="field-label">Pincode</label><input type="text" id="pincode-' + $i + '" name="pincode" placeholder="Pincode" autocomplete="postal-code"></li></ul><div class="ap-drbox"><div class="ap-drbtn"><div class="ap-dradio"><label class="cust-radio-label"><span>Home</span><span class="cust-delivery-text">(All day delivery)</span><input type="radio" id="ship-home' + $i + '" name="ship-' + $i + '" checked="checked"><span class="cust-check"></span></label></div><div class="ap-dradio"><label class="cust-radio-label"><span>Work</span><span class="cust-delivery-text">(Delivery 10:00 AM - 05:00 PM)</span><input type="radio" id="ship-work' + $i + '" name="ship-' + $i + '"><span class="cust-check"></span></label></div></div></div></div><div class="ap-remove-field"><button type="button" class="wt-field-btn ap-remove-ship"><span class="wt-field-icon"><i class="feather-plus"></i></span><span class="wt-field-text">Remove adress</span></button></div></div>';
                    $(this).parent().siblings(".ap-dc").append($dup_ship_html);
                    $i++;
                }
            });
            $dup_input_field.on("click", ".ap-remove-ship", function(e) {
                e.preventDefault();
                $(this).parents(".ap-dcadd-box").remove();
                $x--;
            });
            $ap_dc.on("click", ".wt-ship-box label.cust-checkbox-label input[name]", function() {
                var $ap_box = $(".wt-ship-box label.cust-checkbox-label input[name]"),
                    $ap_dc_ship = $(this).parent().siblings(".ap-dc-ship");
                $ap_box.not(this).prop("checked", false);
                if($ap_box.not(this).prop("checked", false)) {
                    $(".ap-dc-ship").removeClass("hide");
                    $ap_dc_ship.addClass("hide");
                }
                if($(this).prop("checked") == true) {
                    $ap_dc_ship.addClass("hide");
                }
                if($(this).prop("checked") == false) {
                    $ap_dc_ship.removeClass("hide");
                }
            });

            // read-agree js
            $(document).ready(function () {
                $('.cust-checkbox, create-checkbox').on('change', function () {
                    if ($(this).is(':checked')) {
                        $('.checkout, create').removeClass('disabled');
                    } else {
                        $('.checkout, create').addClass('disabled');
                    }
                });
            });

            // billing address
            var $add_bill_btn = $(".ap-add-bill");
            $add_bill_btn.on("click", function() {
                if($x < $max_input_field || $i < $max_input_field) {
                    $x++;
                    var $dup_bill_html = '<div class="ap-dcadd-box wt-bill-box"><h6>Add a new address</h6><label class="cust-checkbox-label"><span>Same as shipping address</span><input type="checkbox" id="ship-address-' + $i + '" name="ship-address-' + $i + '" class="cust-checkbox"><span class="cust-check"></span></label><div class="ap-dc-field ap-dc-bill"><ul class="field-ul"><li class="field-li field-half"><label for="bill-address-' + $i + '" class="field-label">Address</label><input type="text" id="bill-address-' + $i + '" name="address" placeholder="Address" autocomplete="address-line1"></li><li class="field-li field-half"><label for="house-no-' + $i + '" class="field-label">Apartment, suite, etc.</label><input type="text" id="house-no-' + $i + '" name="address" placeholder="Apartment, suite, etc." autocomplete="address-line2"></li><li class="field-li field-half"><label for="country-' + $i + '" class="field-label">Country</label><select id="country-' + $i + '" name="country" autocomplete="country"><option selected="selected" disabled="disabled">--Select your country--</option><option>India</option><option>Uk</option><option>Usa</option><option>Uae</option><option>Bangladesh</option></select></li><li class="field-li field-half"><label for="province-' + $i + '" class="field-label">State</label><input type="text" id="province-' + $i + '" name="province" placeholder="State" autocomplete="address-level1"></li><li class="field-li field-half"><label for="city-' + $i + '" class="field-label">City</label><input type="text" id="city-' + $i + '" name="city" placeholder="City" autocomplete="address-level2"></li><li class="field-li field-half"><label for="pincode-' + $i + '" class="field-label">Pincode</label><input type="text" id="pincode-' + $i + '" name="pincode" placeholder="Pincode" autocomplete="postal-code"></li></ul><div class="ap-drbox"><div class="ap-drbtn"><div class="ap-dradio"><label class="cust-radio-label"><span>Home</span><span class="cust-delivery-text">(All day delivery)</span><input type="radio" id="bill-home' + $i + '" name="bill-' + $i + '" checked="checked"><span class="cust-check"></span></label></div><div class="ap-dradio"><label class="cust-radio-label"><span>Work</span><span class="cust-delivery-text">(Delivery 10:00 AM - 05:00 PM)</span><input type="radio" id="bill-work' + $i + '" name="bill-' + $i + '"><span class="cust-check"></span></label></div></div></div></div><div class="ap-remove-field"><button type="button" class="wt-field-btn ap-remove-bill"><span class="wt-field-icon"><i class="feather-plus"></i></span><span class="wt-field-text">Remove adress</span></button></div></div>';
                    $(this).parent().siblings(".ap-dc").append($dup_bill_html);
                    $i++;
                }
            });
            $dup_input_field.on("click", ".ap-remove-bill", function(e) {
                e.preventDefault();
                $(this).parents(".ap-dcadd-box").remove();
                $x--;
            });
            $ap_dc.on("click", ".wt-bill-box label.cust-checkbox-label input[name]", function() {
                var $ap_box = $(".wt-bill-box label.cust-checkbox-label input[name]"),
                    $ap_dc_bill = $(this).parent().siblings(".ap-dc-bill");
                $ap_box.not(this).prop("checked", false);
                if($ap_box.not(this).prop("checked", false)) {
                    $(".ap-dc-bill").removeClass("hide");
                    $ap_dc_bill.addClass("hide");
                }
                if($(this).prop("checked") == true) {
                    $ap_dc_bill.addClass("hide");
                }
                if($(this).prop("checked") == false) {
                    $ap_dc_bill.removeClass("hide");
                }
            });
        },
        paginationNumber: function() {
            var $page_box = $("#pagination-box");
            $page_box.twbsPagination({
                totalPages: 5,
                visiblePages: 3,
                prev: '<i class="feather-arrow-left"></i>',
                next: '<i class="feather-arrow-right"></i>'
            });
        },
        resizeScreen: function() {
            function resizefullscreen() {
                var $minheight = $window.height(),
                    $resize_screen = $(".fullscreen");
                $resize_screen.css('min-height', $minheight);
            }
            $window.resize(function () {
                resizefullscreen();
            });
            resizefullscreen();
        },
        backTop: function() {
            var $back_top = $("#top"),
                $html_body = $("html, body");
            $window.scroll(function() {
                if($(this).scrollTop() >= 600) {
                    $back_top.addClass("show");
                } else {
                    $back_top.removeClass("show");
                }
            });
            $back_top.on('click', function () {
                $html_body.animate({
                    scrollTop: 0
                }, 100);
                return false;
            });
        }
    };
    WT.animateTemplate = {
        aDelay: 100,
        aQueue: [],
        aTimer: null,
        aBody: null,
        init: function() {
            var $at = this;
                $at.aBody = $body;
                $at.aQueue = [];
                $at.aTimer = null;
            if(typeof aDelay !== 'undefined') {
                $at.aDelay = aDelay;
            }
            $at.aQueue["animate__animated_0"] = [];
            $body.find('#main').find('>div, >section').each(function(index) {
                $(this).attr('data-animated-id', (index + 1));
                $at.aQueue["animate__animated_" + (index + 1)] = [];
            });
            setTimeout(function() {
                $at.registerAnimation();
            }, 200);
        },
        registerAnimation: function() {
            var $at = this;
            $('[data-animate]:not(.animate__animated)', $at.aBody).waypoint(function() {
                var $at_el = this.element ? this.element : this,
                    $this = $($at_el);
                if($this.is(":visible")) {
                    var $at_animated_wrap = $this.closest("[data-animated-id]"),
                        $at_animated_id = '0';
                    if($at_animated_wrap.length) {
                        $at_animated_id = $at_animated_wrap.data('animated-id');
                    }
                    $at.aQueue['animate__animated_' + $at_animated_id].push($at_el);
                    $at.processItemQueue();
                } else {
                    $this.addClass($this.data('animate')).addClass('animate__animated');
                }
            }, {
                offset: '90%',
                triggerOnce: true
            });
        },
        processItemQueue: function() {
            var $at = this;
            if($at.aTimer) {
                return;
            }
            $at.aTimer = window.setInterval(function() {
                var $at_queue = false;
                for(var $at_animated_id in $at.aQueue) {
                    if($at.aQueue[$at_animated_id].length) {
                        $at_queue = true;
                        break;
                    }
                }
                if($at_queue) {
                    for(var $at_animated_id in $at.aQueue) {
                        var $at_item = $($at.aQueue[$at_animated_id].shift());
                        $at_item.addClass($at_item.data('animate')).addClass('animate__animated');
                    }
                    $at.processItemQueue();
                } else {
                    window.clearInterval($at.aTimer);
                    $at.aTimer = null
                }
            }, $at.aDelay);
        }
    };
    /*====================================
        home-1 / home-2 js
    ====================================*/
    WT.mainSlider = {
        init: function() {
            var $home_slider = $("#home-slider, #home-slider2");
            $home_slider.owlCarousel({
                loop: true,
                rewind: false,
                items: 1,
                margin: 0,
                nav: true,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: false,
                singleItem : true,
                autoplay: true,
                autoplayTimeout: 10000,
                autoplayHoverPause: false,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    479: {
                        items: 1,
                        margin: 15
                    },
                    768: {
                        items: 1,
                        margin: 30
                    },
                    979: {
                        items: 1,
                        margin: 30
                    },
                    1199: {
                        items: 1,
                        margin: 30
                    }
                }
            });
            //$('#home-slider .owl-dot')[0].innerHTML = '<div class="inner"></div><div class="bar left-bar"><div class="progress-wrap"></div></div><div class="bar right-bar"><div class="progress-wrap"></div></div>';
            $('#home-slider').on('changed.owl.carousel', function() {
                $('#home-slider').trigger('stop.owl.autoplay');
                $('#home-slider').trigger('play.owl.autoplay');
                var buttons = $('#home-slider .owl-dot');
                for(var button of buttons) {
                    if(button.classList.contains('active')) {
                        button.innerHTML = '<div class="inner"></div><div class="bar left-bar"><div class="progress-wrap"></div></div><div class="bar right-bar"><div class="progress-wrap"></div></div>';
                    } else {
                        button.innerHTML = '<span></span>';
                    }
                }
            });
        }
    };
    /*====================================
        cat slider home-1 / home-2 js
    ====================================*/
    WT.categorySlider = {
        init: function() {
            var $cat_slider = $("#cat-slider");
            $cat_slider.owlCarousel({
                loop: true,
                rewind: true,
                items: 3,
                margin: 30,
                autoplay: true,
                autoplayTimeout: 5000,
                autoplayHoverPause: false,
                nav: true,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: true,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    479: {
                        items: 1,
                        margin: 15
                    },
                    479: {
                        items: 1,
                        margin: 15
                    },
                    540: {
                        items: 2,
                        margin: 15
                    },
                    640: {
                        items: 2,
                        margin: 15
                    },
                    768: {
                        items: 2,
                        margin: 30
                    },
                    979: {
                        items: 3,
                        margin: 30
                    },
                    1199: {
                        items: 3,
                        margin: 30
                    },
                    1299: {
                        items: 4,
                        margin: 30
                    },
                    1399: {
                        items: 4,
                        margin: 30
                    },
                    1499: {
                        items: 4,
                        margin: 30
                    }
                }
            });
        }
    };
    WT.categorySlider = {
        init: function() {
            var $cat_slider = $("#deal-slider2");
            $cat_slider.owlCarousel({
                loop: false,
                rewind: true,
                items: 3,
                margin: 30,
                autoplay: true,
                autoplayTimeout: 5000,
                autoplayHoverPause: true,
                nav: false,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: false,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    479: {
                        items: 2,
                        margin: 15
                    },
                    540: {
                        items: 2,
                        margin: 15
                    },
                    640: {
                        items: 2,
                        margin: 15
                    },
                    768: {
                        items: 2,
                        margin: 30
                    },
                    979: {
                        items: 2,
                        margin: 30
                    },
                    1199: {
                        items: 2,
                        margin: 30
                    },
                    1299: {
                        items: 3,
                        margin: 30
                    },
                    1399: {
                        items: 3,
                        margin: 30
                    },
                    1499: {
                        items: 3,
                        margin: 30
                    }
                }
            });
        }
    };
    /*====================================
        tabSlider home-1 / home-2 js
    ====================================*/
    WT.tabSlider = {
        init: function() {
            this.newTab();
        },
        newTab: function() {
            var swiper = new Swiper(".swiper#new-product", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 2,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-new',
                    nextEl: '.swiper-next-new'
                },
                pagination: {
                    el: ".swiper-pagination-new",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 3,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
    };
    WT.tabSlider2 = {
        init: function() {
            this.newTab();
            this.bestTab();
            this.featureTab();
            this.saleTab();
        },
        newTab: function() {
            var swiper = new Swiper(".swiper#new-product2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 2,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-new2',
                    nextEl: '.swiper-next-new2'
                },
                pagination: {
                    el: ".swiper-pagination-new2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 5,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1499: {
                        slidesPerView: 6,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
        bestTab: function() {
            var swiper = new Swiper('.swiper#best-product2', {
                loop: false,
                rewind: true,
                slidesPerView: 2,
                grid: {
                    rows: 2,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-best2',
                    nextEl: '.swiper-next-best2'
                },
                pagination: {
                    el: ".swiper-pagination-best2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 5,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 6,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 6,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
        featureTab: function() {
            var swiper = new Swiper('.swiper#feature-product2', {
                loop: false,
                rewind: true,
                slidesPerView: 2,
                grid: {
                    rows: 2,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-feature2',
                    nextEl: '.swiper-next-feature2'
                },
                pagination: {
                    el: ".swiper-pagination-feature2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 5,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 5,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 6,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        },
        saleTab: function() {
            var swiper = new Swiper('.swiper#sale-product2', {
                loop: false,
                rewind: true,
                slidesPerView: 2,
                grid: {
                    rows: 2,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-sale2',
                    nextEl: '.swiper-next-sale2'
                },
                pagination: {
                    el: ".swiper-pagination-sale2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 5,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 6,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 6,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    WT.recentSlider = {
        init: function() {
            this.recentProducts();
        },
        recentProducts: function() {
            var swiper = new Swiper(".swiper#recentslider2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-new2',
                    nextEl: '.swiper-next-new2'
                },
                pagination: {
                    el: ".swiper-pagination-new2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 5,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 5,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    WT.featuredSlider = {
        init: function() {
            this.featuredProducts();
        },
        featuredProducts: function() {
            var swiper = new Swiper(".swiper#featuredproducts2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-featured2',
                    nextEl: '.swiper-next-featured2'
                },
                pagination: {
                    el: ".swiper-pagination-featured2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 5,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 6,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 6,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    /*====================================
        trending-pro home-1 / home-2 js
    ====================================*/
    WT.trendingSlider = {
        init: function() {
            this.trendingTab();
        },
        trendingTab: function() {
            var swiper = new Swiper(".swiper#trending-product", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-trending',
                    nextEl: '.swiper-next-trending'
                },
                pagination: {
                    el: ".swiper-pagination-trending",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    WT.trendingSlider2 = {
        init: function() {
            this.trandingProducts();
        },
        trandingProducts: function() {
            var swiper = new Swiper(".swiper#trandingproducts2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-tranding2',
                    nextEl: '.swiper-next-tranding2'
                },
                pagination: {
                    el: ".swiper-pagination-tranding2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 4,
                        grid: {
                            rows: 2,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 4,
                        grid: {
                            rows: 3,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 3,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    WT.trendingSliderinner = {
        init: function() {
            this.trendingTabinner();
        },
        trendingTabinner: function() {
            var swiper = new Swiper(".swiper#tranding-inner2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-trending2',
                    nextEl: '.swiper-next-trending2'
                },
                pagination: {
                    el: ".swiper-pagination-trending2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 5,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    /*====================================
        testi home-1 /home2 js
    ====================================*/
    WT.testimonialsSlider = {
        init: function() {
            var $cat_slider = $("#testimonials-slider");
            $cat_slider.owlCarousel({
                loop: true,
                rewind: true,
                items: 3,
                margin: 30,
                nav: false,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: false,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    479: {
                        items: 1,
                        margin: 15
                    },
                    540: {
                        items: 1,
                        margin: 15
                    },
                    640: {
                        items: 2,
                        margin: 15
                    },
                    768: {
                        items: 2,
                        margin: 30
                    },
                    979: {
                        items: 3,
                        margin: 30
                    },
                    1199: {
                        items: 3,
                        margin: 30
                    },
                    1299: {
                        items: 3,
                        margin: 30
                    },
                    1399: {
                        items: 4,
                        margin: 30
                    },
                    1499: {
                        items: 4,
                        margin: 30
                    }
                }
            });
        }
    };
    WT.testimonialsSlider2 = {
        init: function() {
            var $cat_slider = $("#testimonials-slider2");
            $cat_slider.owlCarousel({
                loop: true,
                rewind: true,
                items: 3,
                margin: 30,
                nav: false,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: false,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    479: {
                        items: 1,
                        margin: 15
                    },
                    540: {
                        items: 1,
                        margin: 15
                    },
                    640: {
                        items: 2,
                        margin: 15
                    },
                    768: {
                        items: 2,
                        margin: 30
                    },
                    979: {
                        items: 3,
                        margin: 30
                    },
                    1199: {
                        items: 3,
                        margin: 30
                    },
                    1299: {
                        items: 3,
                        margin: 30
                    },
                    1399: {
                        items: 4,
                        margin: 30
                    },
                    1499: {
                        items: 4,
                        margin: 30
                    }
                }
            });
        }
    };

    /*====================================
        blog-slider home-1/home-2 js
    ====================================*/
    WT.blogSlider = {
        init: function() {
            var $blog_slider = $("#blog-slider1");
            $blog_slider.owlCarousel({
            loop: true,
            items: 4,
            center: false,
            margin: 30,
            autoplay: true,
            autoplayTimeout: 5000,
            autoplayHoverPause: true,
            nav: false,
            navText: ['<i class="feather-chevron-left"></i>','<i class="feather-arrow-right"></i>'],
            dots: false,
            responsive: {
                0: {
                    items: 1,
                    margin: 12
                },
                479: {
                    items: 1,
                    margin: 12
                },
                540: {
                    items: 1,
                    margin: 12
                },
                640: {
                    items: 2,
                    margin: 12
                },
                768: {
                    items: 2,
                    margin: 30
                },
                1199: {
                    items: 3,
                    margin: 30
                },
                1299: {
                    items: 3,
                    margin: 30
                },
                1499: {
                    items: 3,
                    margin: 30
                }
            }
        });
        }
    };
    WT.blogSlider2 = {
        init: function() {
            this.blogSlider();
        },
        blogSlider: function() {
            var swiper = new Swiper(".swiper#blogslider2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-blogs2',
                    nextEl: '.swiper-next-blogs2'
                },
                pagination: {
                    el: ".swiper-pagination-blogs2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    /*====================================
       brand-logo home-1 /home2 js
    ====================================*/
    WT.brandSlider = {
        init: function() {
            var $cat_slider = $("#brand-slider");
            $cat_slider.owlCarousel({
                loop: true,
                rewind: true,
                items: 3,
                margin: 30,
                nav: false,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: false,
                responsive: {
                    0: {
                        items: 2,
                        margin: 15
                    },
                    320: {
                        items: 2,
                        margin: 15
                    },
                    479: {
                        items: 3,
                        margin: 15
                    },
                    540: {
                        items: 3,
                        margin: 15
                    },
                    640: {
                        items: 4,
                        margin: 15
                    },
                    768: {
                        items: 4,
                        margin: 30
                    },
                    979: {
                        items: 4,
                        margin: 30
                    },
                    1199: {
                        items: 5,
                        margin: 30
                    },
                    1299: {
                        items: 5,
                        margin: 30
                    },
                    1399: {
                        items: 5,
                        margin: 30
                    },
                    1499: {
                        items: 6,
                        margin: 30
                    }
                }
            });
        }
    };
    WT.brandSlider2 = {
        init: function() {
            this.brandSlider();
        },
        brandSlider: function() {
            var swiper = new Swiper(".swiper#brandslider2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 2,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-brands2',
                    nextEl: '.swiper-next-brands2'
                },
                pagination: {
                    el: ".swiper-pagination-brands2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    360: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    540: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    640: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    768: {
                        slidesPerView: 4,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    1024: {
                        slidesPerView: 6,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    },
                    1199: {
                        slidesPerView: 6,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 0
                    }
                }
            });
        }
    };
    /*====================================
       insta home-1 /home2 js
    ====================================*/
    WT.instaSlider = {
        init: function() {
            var $insta_slider = $("#insta-slider");
            $insta_slider.owlCarousel({
                loop: true,
                rewind: true,
                items: 4,
                margin: 0,
                nav: true,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: true,
                singleItem : true,
                autoplay: true,
                autoplayTimeout: 5000,
                autoplayHoverPause: false,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    360: {
                        items: 2,
                        margin: 15
                    },
                    479: {
                        items: 2,
                        margin: 15
                    },
                    540: {
                        items: 3,
                        margin: 15
                    },
                    640: {
                        items: 3,
                        margin: 15
                    },
                    768: {
                        items: 3,
                        margin: 30
                    },
                    979: {
                        items: 3,
                        margin: 30
                    },
                    1199: {
                        items: 4,
                        margin: 30
                    }
                }
            });
        }
    };
    WT.instaSlider2 = {
        init: function() {
            var $insta_slider = $("#insta-slider2");
            $insta_slider.owlCarousel({
                loop: true,
                rewind: true,
                items: 4,
                margin: 0,
                nav: true,
                navText: ['<i class="feather-arrow-left"></i>','<i class="feather-arrow-right"></i>'],
                dots: true,
                singleItem : true,
                autoplay: true,
                autoplayTimeout: 5000,
                autoplayHoverPause: false,
                responsive: {
                    0: {
                        items: 1,
                        margin: 15
                    },
                    360: {
                        items: 2,
                        margin: 15
                    },
                    479: {
                        items: 2,
                        margin: 15
                    },
                    540: {
                        items: 3,
                        margin: 15
                    },
                    640: {
                        items: 3,
                        margin: 15
                    },
                    768: {
                        items: 3,
                        margin: 30
                    },
                    979: {
                        items: 3,
                        margin: 30
                    },
                    1199: {
                        items: 4,
                        margin: 30
                    }
                }
            });
        }
    };
    /*====================================
        dealSlider home-1 /home2 js
    ====================================*/
    WT.dealSlider = {
        init: function() {
            var swiper = new Swiper('.swiper#deal-slider', {
                rewind: true,
                slidesPerView: 1,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column',
                },
                spaceBetween: 0,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-deal',
                    nextEl: '.swiper-next-deal',
                },
                pagination: {
                    el: ".swiper-pagination-deal",
                    clickable: true,
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 12
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 12
                    },
                    360: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 12
                    },
                    540: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 12
                    },
                    640: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 12
                    },
                    768: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1399: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                            spaceBetween: 30
                        },
                    }
                }
            });
        }
    };
    WT.dealSlider2 = {
        init: function() {
            this.dealSlider();
        },
        dealSlider: function() {
            var swiper = new Swiper(".swiper#deal-cat2", {
                loop: false,
                rewind: true,
                slidesPerView: 4,
                grid: {
                    rows: 1,
                    fill: 'row' | 'column'
                },
                spaceBetween: 30,
                observer: true,
                observeParents: true,
                watchSlidesProgress: true,
                navigation: {
                    prevEl: '.swiper-prev-deal2',
                    nextEl: '.swiper-next-deal2'
                },
                pagination: {
                    el: ".swiper-pagination-deal2",
                    clickable: true
                },
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                    pauseOnMouseEnter: true
                },
                breakpoints: {
                    0: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    320: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    360: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    540: {
                        slidesPerView: 1,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    640: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 15
                    },
                    768: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1024: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1199: {
                        slidesPerView: 2,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    },
                    1499: {
                        slidesPerView: 3,
                        grid: {
                            rows: 1,
                            fill: 'row' | 'column',
                        },
                        spaceBetween: 30
                    }
                }
            });
        }
    };
    $document.ready(function() {
        WT.init();
        WT.animateTemplate.init();
        WT.mainSlider.init();
        WT.categorySlider.init();
        WT.tabSlider.init();
        WT.tabSlider2.init();
        WT.trendingSlider.init();
        WT.trendingSlider2.init();
        WT.recentSlider.init();
        WT.featuredSlider.init();
        WT.testimonialsSlider.init();
        WT.testimonialsSlider2.init();
        WT.trendingSliderinner.init();
        WT.brandSlider.init();
        WT.brandSlider2.init();
        WT.instaSlider.init();
        WT.instaSlider2.init();
        WT.dealSlider.init();
        WT.dealSlider2.init();
        WT.blogSlider.init();
        WT.blogSlider2.init();
    });
})(jQuery);