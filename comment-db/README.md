Application : comment-db

Dependencies : json-simple-1.1.1.jar (https://json-simple.googlecode.com/files/json-simple-1.1.1.jar)
Input Data Format : JSON
Input file : /home/chandra/Documents/comment-db/input/data.txt
id : comment id
content : content of the comment
value : Array of user id and parent id.
userId : User id of the author of comment.
parentId : Comment Id to which the reply is posted.

Data Sample:
[{
	"id":1,
	"content":"this is reply to  comment 0",
	"value":["user1",0]
},
{
	"id":2,
	"content":"this is reply to comment 0",
	"value":["user2",0]
}]


Class : CommentDB
Functions

1. getRoot - Get the root of the tree.
2. setRoot - Set the root of the tree.
3. getNumberofChildren - Get the number of children of any given node.
4. findNode - Find the node in the tree based on comment id.
5. startCommentThread - Starts a new comment thread.
6. reply - Add a reply to a given comment id.
7. hasDirectlyReplied - Boolean value if user A has directly replied to user B.
8. hadIndirectlyReplied - Boolean if user A has indirectly replied to user B.
9. getMaxDepth - Get the depth of the Tree.
10. getNodeLevel - Get the level of any node in the tree.
11.getDFS - Constructs a map of a node and its ancestor for each node in the tree.
            Divide the tree into sections and get the ancestor of the nodes.
12. getCommonAncestor - Finds the common ancestor node of 2 given nodes.
13. findNodeByUserId - Finds the node given the User id for the node.
14. getInteractions - Gets the LCA node for the 2 given nodes.

Class : Node
Node properties : commentId, parentId,userId,content
Functions

1. Node - To add node element.
2. addChild - Add a node as a child of a given parent Id.
3. addChildAt - Add a node as a child at a given position.
4. setParent - Set a node as a parent.
5. getParent - Get the parent of a given node.
6. getChildren - Get the children of a given node.
7. getData - Get the comment id of a node.
8. getUser - Get the user id of a node.


Example Usage:

//Create a new Tree.
        Node root = new Node();
        Integer rootcommentId = 0;
        Integer rootParentId = 0;
        Node StartNode = root.Node(rootcommentId.longValue(),rootParentId.longValue(),"user0","0");
        CommentDb commentTree = new CommentDb(StartNode);

// Start comment thread
        startCommentThread(StartNode, commentId, parentId, userId, content);

// Reply to a comment
        commentTree.reply(commentId, parentId, userId, content);

// Has directly replied
        commentTree.hasDirectlyReplied(StartNode,"user5","user6")

// Has indirectly replied
        commentTree.hasIndirectlyReplied(StartNode,"user5","user6")

// Get Interactions
        String interactions = commentTree.getInteractions("user5","user6");

