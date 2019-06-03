var m = require("mithril")
var e = require("../environement/environment")

console.log("jaja",e.BQ_API_URI)

var Petition = {
    list: [],
    loadList: function() {
        return m.request({
            method: "GET",
            url: e.BQ_API_URI+"/getPetition",
            withCredentials: true,
        })
        .then(function(result) {
            Petition.list = result.data
        })
    },

    current:{},
    load : function (id) {
        return m.request({        
            method: "GET",
            url: "https://rem-rest-api.herokuapp.com/api/users/" + id,
            withCredentials: true,
        })
        .then(function(result) {
            Petition.current = result
        })
    },

    save : function (newPetition) {
        m.request({        
            method: "POST",
            url: e.BQ_API_URI+"/createPetition",
            data: {newPetition},
            withCredentials: true,})
        .then(function(data) {
            count = parseInt(data.count)
        })
    }
}

module.exports = Petition