// index.js
var m = require("mithril")

var MyComponent = require("./components/testcomponent/testcomponent")
var PetitionList = require("./views/PetitionList")
var PetitionForm = require("./views/PetitionForm")

m.route(document.body, "/petition",{
    "/petition": PetitionList,
    "/create":PetitionForm
})