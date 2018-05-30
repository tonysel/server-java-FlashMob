var Group = function (title, description, startDate, endDate) {
		this.title = title;
    		this.description = description;
    		this.startDate = startDate;
    		this.endDate = endDate;
    		
    	}
    	

	Group.prototype.setFromJSON = function(json) {
	  this.title=json.title;
	  this.description=json.description
	  this.startDate=json.startDate;
	  this.endDate=json.endDate;
	};
    	

	Car.prototype.toString = function() {
		return "Group [title=" + this.title + ", description=" + this.description + ", startDate=" + this.startDate + ", endDate=" + this.endDate + "]";	
    };