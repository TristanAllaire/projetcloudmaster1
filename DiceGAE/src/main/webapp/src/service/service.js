var m = require ("mithril"),
var PetitionService  = {
    createPetition : function (newPetition) {
        m.request({        
            method: "POST",
            url: BQ_API_URI+"/createPetition",
            data: {newPetition},
            withCredentials: true,})
        .then(function(data) {
            count = parseInt(data.count)
        })
    }
}