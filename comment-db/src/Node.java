/**
 * Created by chandra on 15/12/05.
 */
import java.util.*;

public class Node {
    private List<Node> children = null;
    private Node parent;
    private Long commentId;
    private Long parentId;
    private String userId;
    private String content;

    public Node Node(Long commentId, Long parentId, String userId, String content)
    // Create a new node for the tree.
    {
        this.commentId = commentId;
        this.parentId = parentId;
        this.userId = userId;
        this.content = content;
        this.children = new ArrayList<Node>();
        return this;
    }
    public void addChild(Node child){
        //Add a node as a child to another node.
        child.setParent(this);
        this.children.add(child);
    }

    public void setParent(Node parent)
    // Set a node as a parent to another node.
    {
        this.parent = parent;
    }

    public Node getParent()
    // Get the parent of a node.
    {
        return this.parent;
    }

    public List<Node> getChildren()
    // Get the children of a node.
    {
        return this.children;
    }

    public Long getData()
    // Get the commentId from a given node.
    {
        return this.commentId;
    }

    public String getUser()
    // Get the userId for a given node.
    {
        return this.userId;
    }

}
