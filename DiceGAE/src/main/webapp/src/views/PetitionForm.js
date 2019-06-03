var m = require("mithril").default
var Petition = require("../models/Petition")

module.exports = {
    view: function() {
        return m("form", {
            onsubmit: function(e){
                e.preventDefault()
                Petition.save()
            }
        }, [
            m("label.label", "Nom de la pétition"),
            m("input.input[type=text][placeholder=Veuillez entrer un nom de pétition]"),
            m("button.button[type=button]", "Enregistrer"),
        ])
    }
}