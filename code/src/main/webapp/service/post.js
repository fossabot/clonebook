class Post {
	constructor(id,media,page,text,user,voteTotal) {
		this.id=id;
		this.media=media;
		this.page=page;
		this.text=text;
		this.user=user;
		this.voteTotal=voteTotal;
	}
	static getPost(id) {
		Utils.sendGet("/rest/post/"+id).then(Post.fromRaw)
	}
	static fromRaw(raw) {
		return new Post(raw.id,raw.media,raw.page,raw.text,raw.user,raw.voteTotal);
	}
}