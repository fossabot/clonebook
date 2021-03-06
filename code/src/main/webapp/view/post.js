let lastSelectedField;
let currentPage;
let posts={};
function addPost(post,prepend) {
	posts[post.id]=post;
	let node = $('#postTemplate').contents("article").clone();
	node.attr("id",post.id);
	if(post.user.id==getLoggedInId()) node.addClass("own");
	node.find('.profilePicture').attr('src',Media.fromId(post.user.profilePicture).getUrl());
	node.find('.name').text(post.user.name);
	node.find('.name').attr('href','#user='+post.user.id);
	node.find('.text').text(post.text);
	const momentObj=moment(post.date);
	node.find('.dateHoverEvent').attr('date',post.date);
	node.find('.date').text(momentObj.fromNow());
	node.find('.dateHover').text(momentObj.format('LLLL'));
	node.find('.points').text(post.voteTotal);
	post.media.forEach(media=>{
		node.find('.media').append('<a href="'+media.getUrl()+'"><img class="mediaImage" alt="'+post.user.name+" media object"+'" src="'+media.getUrl()+'"></a>');
	});
	let field=$(post.repliedTo?'#'+post.repliedTo+' > .subReplies':'#posts');
	if(prepend) field.prepend(node);
	else field.append(node);
	post.children.forEach(child=>{
		addPost(child,prepend);
	})
}
$(document).on('click','.vote',event=>{
	Utils.sendPost('post/'+$(event.currentTarget).parent().parent().attr('id')+'/vote',generateFormData({vote:$(event.target).attr('action')}))
		.then(response=>$(event.currentTarget).parent().parent().find('.points').text(response.punten));
});
$(document).on('click','.replyButton',(event)=>{
	if($(event.target).parent().parent().find('.messageForm').length>0) $(event.target).parent().parent().find('.messageForm').toggle();
	else {
		clone=$('#messageBoxTemplate').contents("div").clone();
		clone.find('[name="repliedTo"]').val($(event.target).parent().parent().attr('id'));
		clone.find('[name="pageId"]').val(currentPage.id);
		$(event.target).parent().after(clone);
	}
});
$(document).on('click','.newFile',event=>{
	$(event.target).parent().find('.files').append('<input type="file" accept="image/*" class="fileUpload">');
});
$(document).on('submit','.messageForm',event=>{
	event.preventDefault();
	Promise.all($(event.target).find('.files').find('[type="file"]').map((id,file)=>{
		if(!file.files[0]) return Promise.resolve();
		if(file.files[0].size>5*1024*1024) return Promise.reject(413);
		return Media.create(file.files[0]).then(obj=>{
			$(file).attr('type','hidden');
			$(file).attr("name","file");
			$(file).val(obj.id);
		});
	})).catch(message=>{
		if(message===413) alert('Bestand te groot!');
		if(message===415) alert("bestandstype niet toegestaan.");
		if(message)
		throw new Error("FileUploadException");
	}).then(()=>{
		$(event.target).find('[name="pageId"]').val(currentPage.id);
		return Utils.sendPost('post',generateFormData(event.target));
	}).then(Post.fromRaw).then(post=>{
		$(event.target).parent().remove();
		alert('bericht gepost!');
		addPost(post,true);
	});
});
function showSinglePost(postId) {
	Post.getPost(postId).then(post=>{
		$('#posts').html('');
		addPost(post);
		autoChanged();
		window.location.hash='#post='+post.id;
		return showPageHeader(post.pageId);
	},message=>{
		if(message==404) message={pageId:"NotFound"};
		if(message.pageId) showPageHeader(message.pageId);
		else throw message;
	})
}
function logoutPost() {
	$('#posts').html('');
	$('#pageHeader > span').text("");
	$("#page").hide();
}
$(document).on('click','.deletePost',event=>{
	if(!confirm("weet u zeker dat u dit bericht wilt verwijderen?")) return;
	posts[$(event.currentTarget).parent().parent().attr('id')].delete().then(()=>{
		$(event.currentTarget).parent().parent().remove();
	});
})