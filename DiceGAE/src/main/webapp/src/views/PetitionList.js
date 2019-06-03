var m = require("mithril").default
var Petition = require("../models/Petition")

module.exports = {
    oninit: Petition.loadList,
    view: function() {
        return m(".user-list", Petition.list.map(function(user) {
            return m(".user-list-item", user.firstName + " " + user.lastName)
        }))
    }
}
