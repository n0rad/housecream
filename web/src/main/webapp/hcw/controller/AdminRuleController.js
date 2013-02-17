define(['hcw/service/RuleService', 'hcw/view/admin/rule/RuleForm', 'hcw/view/admin/rule/RuleTable'], 
function(RuleService, RuleForm, RuleTable) {
    
    function RuleController(rootUrl, context) {
        this.ruleService = new RuleService("/hcs");
        this.ruleForm = new RuleForm(rootUrl, context);
        this.ruleTable = new RuleTable(rootUrl, context);
    }
    
    RuleController.prototype = {
            displayForm : function(ruleId) {
                var self = this;
                this.ruleService.getRule(ruleId, function(data) {
                    self.ruleForm.displayForm(data);
                });
            },
            
            displayNewForm : function() {
                this.ruleForm.displayForm();
            },
    
            displayTable : function() {
                var self = this;
                this.ruleService.getRules(function(data) {
                    self.ruleTable.displayTable(data);
                });
            }
    };
    
    return RuleController;
});
