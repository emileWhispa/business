//has uppercase
window.Parsley.addValidator('uppercase', {
    requirementType: 'number',
    validateString: function (value, requirement) {
        var uppercases = value.match(/[A-Z]/g) || [];
        return uppercases.length >= requirement;
    },
    messages: {
        en: 'Your password must contain at least (%s) uppercase letter.'
    }
});

//has lowercase
window.Parsley.addValidator('lowercase', {
    requirementType: 'number',
    validateString: function (value, requirement) {
        var lowecases = value.match(/[a-z]/g) || [];
        return lowecases.length >= requirement;
    },
    messages: {
        en: 'Your password must contain at least (%s) lowercase letter.'
    }
});

//has number
window.Parsley.addValidator('number', {
    requirementType: 'number',
    validateString: function (value, requirement) {
        var numbers = value.match(/[0-9]/g) || [];
        return numbers.length >= requirement;
    },
    messages: {
        en: 'Your password must contain at least (%s) number.'
    }
});

//has special char
window.Parsley.addValidator('special', {
    requirementType: 'number',
    validateString: function (value, requirement) {
        var specials = value.match(/[^a-zA-Z0-9]/g) || [];
        return specials.length >= requirement;
    },
    messages: {
        en: 'Your password must contain at least (%s) special characters.'
    }
});


function formLoader(form) {
    var cCheck = form.querySelector(".loading-pro");
    if (cCheck) $(cCheck).fadeIn();
    else {
        var loading = document.createElement("div");
        $(loading).addClass("formLoader");
        $(loading).addClass("loading-pro");
        var topBar = document.createElement("div");
        topBar.className = "load-bar absolute top-left";
        topBar.innerHTML = "<div class=\"bar\"></div><div class=\"bar\"></div><div class=\"bar\"></div><div class=\"bar\"></div>";
        loading.appendChild(topBar);
        var loader2 = document.createElement("div");
        loader2.className = "loader-new didHide";
        loading.appendChild(loader2);
        setInterval(function () {
            $(topBar).slideToggle();
            $(loader2).slideToggle();
        }, 9000);
        var innerLoad = document.createElement("div");
        $(innerLoad).addClass("formLoading");
        loading.appendChild(innerLoad);

        form.insertBefore(loading, form.firstChild);

        $(loading).fadeIn();

        cCheck = loading;
    }
    return {
        parent: cCheck,
        hide: function () {
            $(this.parent).fadeOut();
        },
        show: function () {
            $(this.parent).fadeIn();
        }
    };
}

function getModal(params) {
    $("#main_modal").remove();
    var modal = document.createElement("div");
    modal.id = "main_modal";
    modal.className = "uk-modal";

    var dialog = document.createElement("div");
    dialog.className = "uk-modal-dialog relative";

    var header = document.createElement("uk-modal-header");
    header.className = "uk-modal-header";
    var title = document.createElement("h3");
    title.className = "uk-modal-title";
    var spanTitle = document.createElement("tag");
    spanTitle.textContent = params.title;

    var icon = document.createElement("i");
    icon.className = "material-icons";
    icon.setAttribute("data-uk-tooltip", "{pos:'middle'}");
    icon.title = params.title;
    icon.innerHTML = "&#xE8FD";

    var error = document.createElement("div");
    error.className = "text-red";
    var body = document.createElement("div");
    body.className = "uk-modal-body";
    body.innerHTML = params.content ? params.content : "";

    var app = document.createElement("div");

    if( params.appends ){
        params.appends.forEach(function (value) {
            app.appendChild(value);
        });
    }

    var footer = document.createElement("div");
    footer.className = "uk-modal-footer uk-text-right";

    var but = document.createElement("button");
    but.className = "md-btn md-btn-flat uk-modal-close";
    but.innerHTML = "Close";

    var save = document.createElement("button");
    save.className = "md-btn md-btn-flat md-btn-flat-primary";
    save.innerHTML = params.buttonTitle ? params.buttonTitle : "Continue";
    footer.appendChild(but);
    if (params.buttons) {
        params.buttons.forEach(function (v) {

            var cv = document.createElement("button");
            cv.className = "md-btn md-btn-flat md-btn-flat-primary";
            cv.innerHTML = v.title;
            cv.onclick = function (ev) {
                ev.preventDefault();
              if( v.func ) v.func();
            };
            footer.appendChild(cv);
        });
    }

    if( !params.noSave)
    footer.appendChild(save);


    title.appendChild(spanTitle);
    title.appendChild(icon);

    header.appendChild(title);

    dialog.appendChild(header);
    dialog.appendChild(error);
    dialog.appendChild(body);
    dialog.appendChild(app);
    dialog.appendChild(footer);

    modal.appendChild(dialog);

    document.body.appendChild(modal);

    return {
        parent: modal,
        dialog: dialog,
        body: body,
        button: save,
        error: error,
        spanTitle: spanTitle,
        show: function () {
            var eventModal = UIkit.modal(this.parent);
            eventModal.show();
        },
        updateTitle: function (res) {
            this.spanTitle.textContent = res;
        },
        updateError: function (res) {
            var e = this.error;
            e.textContent = res;
            var div = document.querySelector("#main_modal");
            if( div ){
                div.scrollTop = 0;
            }
            console.log("error handling");
            setTimeout(function () {
                e.textContent = "";
            }, 12000);
        }
    };
}

function getScrollParent(node) {
    if (node == null) {
        return null;
    }

    if (node.scrollHeight > node.clientHeight) {
        return node;
    } else {
        return getScrollParent(node.parentNode);
    }
}

$(document).on('click', '.delete-launcher', function (e) {
    e.preventDefault();

    var md = getModal({title: this.title, content: "Are you sure to delete selected ?"});

    md.show();


    var form = document.createElement("form");

    var arr = document.querySelectorAll(".check_row,input[name=csrfToken]");


    arr.forEach(function (v) {
        form.appendChild(v.cloneNode(true));
    });

    var href = this.href;
    md.button.onclick = function (ev) {
        ev.preventDefault();
        var ld = formLoader(md.dialog);

        ld.show();
        $.ajax({
            method: "POST",
            url: href,
            data: $(form).serialize(),
            success: function () {
                ld.hide();
                window.location.reload();
            }
        });

    }

});

$(document).on('click', '.pop-launcher', function (e) {
    e.preventDefault();

    var t = textarea();
    t.setLabel("Add comment here.");
    t.setHolder("text here ...");

    var ref = $(this).attr("data-href");
    var md = getModal({
        title: this.title,
        content: $(this).attr("data-content"),
        buttonTitle: $(this).attr("data-button"),
        appends: [t.parent],
        buttons: [
            {
                title: "Reject",
                func: function () {

                    var lda = formLoader(md.dialog);

                    lda.show();
                    $.ajax({
                        method: "GET",
                        url: ref+"?value="+encodeURIComponent(t.input.value),
                        success: function (res) {
                            lda.hide();
                            if (res === "1")
                                window.location.reload();
                            else md.updateError(res);
                        }
                    });
                }
            }
        ]
    });

    md.show();


    var href = this.href;
    md.button.onclick = function (ev) {
        ev.preventDefault();
        var ld = formLoader(md.dialog);

        ld.show();
        $.ajax({
            method: "GET",
            url: href+"?value="+encodeURIComponent(t.input.value),
            success: function (res) {
                ld.hide();
                if (res === "1")
                    window.location.reload();
                else md.updateError(res);
            }
        });

    }

});


$(document).on('click', '.x-launcher', function (e) {
    e.preventDefault();

    var md = getModal({
        title: this.title,
        noSave:true,
        content: $(this).attr("data-value")
    });

    md.show();



});

function addOrUpdateUrlParam(name, value) {
    var href = window.location.href;
    var regex = new RegExp("[&\\?]" + name + "=");
    if (regex.test(href)) {
        regex = new RegExp("([&\\?])" + name + "=\\d+");
        href = href.replace(regex, "$1" + name + "=" + value);
    } else {
        if (href.indexOf("?") > -1)
            href = href + "&" + name + "=" + value;
        else
            href = href + "?" + name + "=" + value;
    }

    window.location.href = href;

    return false;
}

$(document).on('click', '.launcher-app', function (e) {

    e.preventDefault();
    var md = getModal({title: this.title,content:$(this).attr("data-content"), buttonTitle: $(this).attr("data-button")});
    md.show();
    var href = this.href;
    md.button.onclick = function () {
        window.location = href;
    };
});

$(document).on('click', '.launcher', function (e) {
    e.preventDefault();
    var md = getModal({title: this.title, buttonTitle: $(this).attr("data-button")});
    md.show();

    var ld = formLoader(md.dialog);

    $.ajax({
        method: "GET",
        url: this.href,
        success: function (json) {
            ld.hide();


            var c = new Controller();
            var head = json["formHead"];
            var token = head.token;
            var o = {
                title: head["formName"],
                route: head["saveRoute"],
                tokenName: token.tokenName,
                tokenValue: token.tokenValue
            };

            md.updateTitle(o.title);

            var form = c.CreateForm(o);
            form.addObject(json.formData);

            md.body.appendChild(form.form);

            md.button.onclick = function (ev) {
                ev.preventDefault();

                c.validate(form.fieldsArray, function () {
                    ld.show();
                    form.success = function (e) {
                        ld.hide();
                        if (e === "1") {
                            form.form.reset();
                            window.location.reload();
                        } else {
                            form.updateError(e);
                        }
                    };
                    c.saveForm(form);
                });
            }
        }
    });

    ld.show();
});

function validateRole(form) {

    var ld = formLoader(form);

    $.ajax(
        {
            type: "POST",
            url: form.action,
            data: $(form).serialize(),
            success: function (res,s,xhr) {
                if (res !== "1")
                    console.log(res);
                else window.location.href = xhr.getResponseHeader("url");
            },
            complete: function () {
                ld.hide();
            }
        }
    );
    return false;
}

function saveNew(form) {
    var ld = formLoader(form);
    $.ajax(
        {
            type: "POST",
            url: form.action,
            data: $(form).serialize(),
            success: function (res) {
                $(".sign-up-error").html(res);
            },
            complete: function () {
                ld.hide();
            }
        }
    );
    return false;
}

function changePayment(elem) {
    $(".bank-field,.mobile-field").addClass("uk-hidden");
    var obj = $("#paymentSlip,#file_upload-select,#paymentMobile");
    obj.attr("required", "required");
    if (elem.value === "0") {
        obj.removeAttr("required");
    } else if (elem.value === "1") {
        $("#paymentMobile").removeAttr("required");
        $(".bank-field").removeClass("uk-hidden");
    } else if (elem.value === "2") {
        $("#file_upload-select,#paymentSlip").removeAttr("required");
        $(".mobile-field").removeClass("uk-hidden");
    }
}

function dError(form, res) {
    $("body,html").animate({scrollTop: 0}, 'slow');
    $(form).find(".form_error").html(res);
}

function saveMb(form, evt , clb) {
    if ($(form).parsley().validate()) {
        evt.preventDefault();
        var ld = formLoader(form);

        var controller = new Controller();
        controller.saveForm({
            form: form,
            success: function (res, xhr) {
                var url = xhr.getResponseHeader("url");
                if (url !== null) {
                    window.location.href = url;
                } else {
                    dError(form, res);
                }
                ld.hide();

                if( controller.isFunc(clb) ) clb(res);
            }
        });


    }
    return false;
}

function saveMb2(but, evt) {
    var ld = formLoader(but.parentNode);
    ld.show();
    var form = but.form;
    form.action = $(form).attr("data-action");
    saveMb(but.form,evt,function () {
        ld.hide();
    });
    return false;
}

function saveMbUp(form, evt) {
    if ($(form).parsley().validate()) {
        evt.preventDefault();
        var ld = formLoader(form);

        var controller = new Controller();
        controller.saveForm({
            form: form,
            success: function (res, xhr) {
                var url = xhr.getResponseHeader("url");


                var eventModal = UIkit.modal("#modal_header_footer_2");
                eventModal.show();

                ld.hide();
            }
        });


    }
    return false;
}

function checkP( form ) {

    if ( $(form).parsley().validate() ) {

        var ld = formLoader(form);
        ld.show();

        $.ajax({
            url:form.action,
            data:$(form).serialize(),
            method:"POST",
            success:function (res,status,xhr) {
                if( xhr.getResponseHeader("success") ){
                    $(".m-content").html(res);
                }else $(".m-error").text(res);
            },
            complete:function () {
                ld.hide();
            }
        });

    }

    return false;
}

function login(form) {
    var ld = formLoader(form);
    $.ajax(
        {
            type: "POST",
            url: form.action,
            data: $(form).serialize(),
            success: function (res) {
                $(".uk-modal-body").html(res);
            },
            complete: function () {
                ld.hide();
                var eventModal = UIkit.modal("#modal_header_footer");
                eventModal.show();
            }
        }
    );
    return false;
}

function loadRoles(form) {
    var vim = document.querySelector("#modal_header_footer");
    if (vim) {
        var ld = formLoader(vim);
        var eventModal = UIkit.modal(vim);
        eventModal.show();
        $.ajax(
            {
                type: "GET",
                url: form.href,
                success: function (res) {
                    $(".uk-modal-body").html(res);
                },
                complete: function () {
                    ld.hide();
                }
            }
        );
    }
    return false;
}