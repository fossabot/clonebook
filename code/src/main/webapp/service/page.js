class Page {
	constructor(id,last10Posts,logo,name,owner) {
		this.id=id;
		this.last10Posts=last10Posts;
		this.logo=logo;
		this.name=name;
		this.owner=owner;
	}
	static getPage(id) {
		return Utils.sendGet("page/"+id).then(Page.fromRaw);
	}
	static fromRaw(raw) {
		raw.last10Posts=raw.last10Posts.map(sourcePost=>Post.fromRaw(sourcePost));
		return new Page(raw.id,raw.last10Posts,raw.logo,raw.name,raw.owner);
	}
	static new(name) {
		return Utils.sendPost('page',generateFormData({name:name})).then(response=>response.id)
	}
	askPermissions() {
		return Utils.sendPost('page/'+this.id+'/lidAanvraag','');
	}
	acceptLid(user) {
		return Utils.sendPost('page/'+this.id+'/acceptLid/'+user.id,'');
	}
	denyLid(user) {
		return Utils.sendDelete('page/'+this.id+'/lidAanvraag/'+user.id,'');
	}
	getLeden() {
		console.log('getleden',this);
		return Utils.sendGet('page/'+this.id+'/leden').then(ledenData=>{
			return ledenData.map(User.fromRaw);
		})
	}
	setIcon(media) {
		let formData = new FormData();
		formData.append("image",media.id);
		return Utils.sendPost('page/'+this.id+'/image',formData)
	}
	setName(name) {
		this.name=name;
		let formData = new FormData();
		formData.append("name",name);
		return Utils.sendPost('page/'+this.id+'/name',formData)
	}
	isAdmin() {
		if(!this.owner) return false;
		return this.owner.id==getLoggedInId();
	}
}