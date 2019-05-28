var m = require ("mithril"),
var Service  = {
    createPetition : function (newPetition) {
        m.request({        
            method: "POST",
            url: "//lepetitcloud.appspot.com/_ah/api/petitions/v1/createPetition",
            data: {newPetition},
            withCredentials: true,})
        .then(function(data) {
            count = parseInt(data.count)
        })
    }
}