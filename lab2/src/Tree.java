import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Tree {
    public String name;
    public List<Tree> children;

    public Tree(String name, Tree... children) {
        this.name = name;
        this.children = Arrays.asList(children);
    }
    public Tree(String name) {
        this.name = name;
    }
    private static class Pair {
        int id;
        Tree val;
        Pair(int id, Tree val) {
            this.id = id;
            this.val = val;
        }
        public int getId() {
            return id;
        }
        public Tree getTree() {
            return val;
        }
    }
    public boolean equals(Tree other) {
        if (other != null && name.equals(other.name) && ((children == null && other.children == null) || (children.size() == other.children.size()))) {
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    if (!children.get(i).equals(other.children.get(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    //dot -Tpng -o tree.png example.dot
    public static void printTree(Tree root) {
        StringBuilder data = new StringBuilder("digraph Tree {\n");
        Queue<Pair> q = new LinkedList<>();
        int cnt = 0;
        q.add(new Pair(cnt++, root));
        data.append(Integer.toString(cnt - 1)).append(" [label=\"").append(root.name).append("\"];\n");
        while (!q.isEmpty()) {
            Pair cur = q.remove();
            if (cur.getTree().children != null) {
                for (int i = 0; i < cur.getTree().children.size(); i++) {
                    Tree curSon = cur.getTree().children.get(i);
                    q.add(new Pair(cnt++, curSon));
                    data.append(Integer.toString(cnt - 1)).append(" [label=\"").append(curSon.name).append("\"];\n");
                    data.append(Integer.toString(cur.getId())).append(" -> {").append(cnt - 1).append("};\n");
                }
            }
        }
        data.append("}");
        try {
            FileWriter temp = new FileWriter("./src/output.dot");
            temp.write(data.toString());
            temp.close();
        } catch (IOException e) {
            System.out.println("Unable to write to output.dot file for visualization");
        }
    }
}
