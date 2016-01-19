/**
 * Created by chandra on 15/12/05.
 */
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.lang.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CommentDb {

    private Node root;
    public int MaxDepth = 0;
    private Map<Node,Node> ancestorMap = new HashMap<Node,Node>();

    public CommentDb(Node root){
        this.root = root;
    }

    public Node getRoot(){
        //Get the root of the tree.
        return this.root;
    }

    public void setRoot(Node root){
        //Set the root of the tree.
        this.root= root;
    }

    public static Integer getNumberofChildren(Node node){
        //Get the number of children of a node.
         return node.getChildren().size();
    }

    public static Node findNode(Node node, Long NodeId)
            //Find the node based on the comment Id.
    {
        if(node == null)
            return null;
        if(node.getData().equals(NodeId))
            return node;
        else
        {
            Node cnode = null;
            for (Node child : node.getChildren()) {
                if ((cnode = findNode(child, NodeId)) != null) {
                    return cnode;
                }
            }
        }
        return null;
    }

    public static Node startCommentThread(Node StartNode,Long commentId,
                                          Long parentId, String userId, String content){
        //Start a new comment thread adding it as a child to the root node.
        Node comment = new Node();
        Node commentAdded = comment.Node(commentId,parentId,userId,content);
        StartNode.addChild(commentAdded);
        return commentAdded;
    }

    public  Node reply(Long commentId, Long parentId, String userId, String content){
        // Add a reply to a comment, provided the parentId to which to add.
        Node comment = new Node();
        Node ParentNode = findNode(this.root,parentId);
        Node replyAdded = comment.Node(commentId, parentId, userId, content);
        ParentNode.addChild(replyAdded);
        return replyAdded;
    }

    public boolean hasDirectlyReplied(Node searchNode, String userOne, String userTwo){
        //Check if a comment of UserOne is a parent of comment of UserTwo and vice versa.

        if(searchNode == null)
            return false;
        if(searchNode.getUser() == userOne) {
            for (Node child : searchNode.getChildren()) {
                if (child.getUser() == userTwo)
                    return true;
            }
        }else
        {
            for (Node child : searchNode.getChildren()) {
                if (hasDirectlyReplied(child, userOne, userTwo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasIndirectlyReplied(Node searchNode, String userOne, String userTwo ){
        // Check if  a comment is from userOne or userTwo and check the children of that comment if userOne
        // or userTwo exists.
        if(searchNode == null)
            return false;
        if(searchNode.getUser() == userOne || searchNode.getUser() == userTwo) {
            for (Node child : searchNode.getChildren()) {
                if (child.getUser() == userOne || searchNode.getUser() == userTwo &&
                        child.getUser() != searchNode.getUser())
                    return true;
                else {
                    for (Node nextChild : child.getChildren()) {
                       hasIndirectlyReplied(nextChild, userOne, userTwo);
                    }
                }
            }
        }
        else
        {
            for (Node child : searchNode.getChildren()) {
                if (hasIndirectlyReplied(child, userOne,userTwo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void getMaxDepth(Node node)
            //Get the depth of the tree based on the largest node level.
    {
        int max = 0;
        if(node.getChildren().size() == 0) {
            int level = getNodeLevel(node);
            if (max < level) {
                max = level;
                MaxDepth = max;
            }
        }
        if(node.getChildren().size() > 0) {
            for (Node child : node.getChildren()) {
                 getMaxDepth(child);
            }
        }
    }

    public Integer getNodeLevel(Node n){
        //Get the node level of a given node.
        int level = 0;
        if(n == this.root){
            level = 0;
        }else {
            while (n != null) {
                level += 1;
                n = n.getParent();
            }
        }
        return level;
    }

    public void getDFS(Node n,Map<Node,Node> ancestorMap){
        // Construct the map for a node and it's ancestor. The tree is divided into sqrt(height of tree) + 1  sections.
        // Each node belongs to a section. The nodes with level 1 have the root as ancestor.
        // The nodes which are at the upper boundary have the last level node of the
        // previous section as ancestor which is their direct parent.
        // The nodes at lower boundary of the section have their ancestor of their parent as their ancestor.
        Node Ancestor = null;
        getMaxDepth(this.root);
        double height = MaxDepth;
        double sqrtheight = Math.sqrt(height);
        double nodeLevel= getNodeLevel(n);

        if(nodeLevel < sqrtheight) {
             Ancestor = getRoot();
             ancestorMap.put(n,Ancestor);
        }else {
            double rem = nodeLevel % sqrtheight;
            if (rem == 0) {
                Ancestor = n.getParent();
                ancestorMap.put(n,Ancestor);
            } else {
                Ancestor = ancestorMap.get(n.getParent());
            }
        }
        for(Node child : n.getChildren())
            getDFS(child,ancestorMap);
    }

    public Node getCommonAncestor(Node userOne, Node userTwo){
        //Get the Lowest Common Ancestor based on the map created for the nodes and their ancestors.
        // The lower level node is moved up until the ancestors are the same for both nodes.

        getDFS(this.root, ancestorMap);

        while(ancestorMap.get(userOne) != ancestorMap.get(userTwo)) {
            if (getNodeLevel(userOne) > getNodeLevel(userTwo)) {
                userOne = ancestorMap.get(userOne);
            } else
                userTwo = ancestorMap.get(userTwo);
        }
        // userOne and userTwo are in the same section..
        while(userOne != userTwo){
               if(getNodeLevel(userOne) > getNodeLevel(userTwo)) {
                   userOne = userOne.getParent();
                }
                else{
                   userTwo = userTwo.getParent();
               }
            }
        return userOne;
    }

    public static Node findNodeByUserId(Node node, String UserId)
            //Find the node given the user id.
    {
        if(node == null)
            return null;
        if(node.getUser().equals(UserId)) {
            return node;
        }
        else
        {
            Node cnode = null;
            for (Node child : node.getChildren()) {
                if ((cnode = findNodeByUserId(child, UserId)) != null) {
                    return cnode;
                }
            }
        }
        return null;
    }

    public String getInteractions(String userOne, String userTwo){
        //Get the userId of the LCA node.
        Node userOneNode = findNodeByUserId(this.root,userOne);
        Node userTwoNode = findNodeByUserId(this.root,userTwo);
        Node commonAncestor = getCommonAncestor(userOneNode,userTwoNode);
        return commonAncestor.getUser();
     }

    public static void main(String[] args){

        Node root = new Node();
        Integer rootcommentId = 0;
        Integer rootParentId = 0;
        Node StartNode = root.Node(rootcommentId.longValue(),rootParentId.longValue(),"user0","0");
        CommentDb commentTree = new CommentDb(StartNode);

        try{
            FileReader reader = new FileReader("/home/chandra/Documents/comment-db/input/data.txt");
            JSONParser inputParser = new JSONParser();
            JSONArray myArray = (JSONArray) inputParser.parse(reader);
            Iterator itr = myArray.iterator();
            while(itr.hasNext()){
                JSONObject thisObject = (JSONObject) itr.next();
                Long commentId = (Long) thisObject.get("id");
                String content = thisObject.get("content").toString();
                JSONArray value = (JSONArray) thisObject.get("value");
                String userId = (String) value.get(0);
                Long parentId = (Long) value.get(1);
                /*Adding data to the tree*/
                if (parentId == 0){
                     startCommentThread(StartNode, commentId, parentId, userId, content);
                }else{
                    Node replynodeadded = commentTree.reply(commentId, parentId, userId, content);
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}

