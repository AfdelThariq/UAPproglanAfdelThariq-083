import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Recipe {
    private String name;
    private String ingredients;

    public Recipe(String name, String ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}

public class RecipeManager extends JFrame {
    private ArrayList<Recipe> recipes;
    private DefaultTableModel tableModel;
    private JTable table;

    public RecipeManager() {
        recipes = new ArrayList<>();
        setTitle("Recipe Manager");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Customize UI
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 14));
        UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 12));
        UIManager.put("Table.gridColor", Color.LIGHT_GRAY);
        UIManager.put("Panel.background", Color.WHITE);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Name", "Ingredients"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = new JButton("Add Recipe");
        JButton editButton = new JButton("Edit Recipe");
        JButton deleteButton = new JButton("Delete Recipe");
        JButton searchButton = new JButton("Search");

        JTextField searchField = new JTextField(20);
        buttonPanel.add(new JLabel("Search:"));
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecipe();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRecipe();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecipe();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText().toLowerCase();
                tableModel.setRowCount(0);
                for (Recipe recipe : recipes) {
                    if (recipe.getName().toLowerCase().contains(keyword)) {
                        tableModel.addRow(new Object[]{recipe.getName(), recipe.getIngredients()});
                    }
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
    }

    private void addRecipe() {
        try {
            JTextField nameField = new JTextField();
            JTextField ingredientsField = new JTextField();

            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.add(new JLabel("Name:"));
            formPanel.add(nameField);
            formPanel.add(new JLabel("Ingredients:"));
            formPanel.add(ingredientsField);

            int option = JOptionPane.showConfirmDialog(this, formPanel, "Add Recipe", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (nameField.getText().isEmpty() || ingredientsField.getText().isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                Recipe recipe = new Recipe(nameField.getText(), ingredientsField.getText());
                recipes.add(recipe);
                tableModel.addRow(new Object[]{recipe.getName(), recipe.getIngredients()});
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRecipe() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                throw new IllegalArgumentException("No recipe selected.");
            }

            Recipe recipe = recipes.get(selectedRow);

            JTextField nameField = new JTextField(recipe.getName());
            JTextField ingredientsField = new JTextField(recipe.getIngredients());

            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.add(new JLabel("Name:"));
            formPanel.add(nameField);
            formPanel.add(new JLabel("Ingredients:"));
            formPanel.add(ingredientsField);

            int option = JOptionPane.showConfirmDialog(this, formPanel, "Edit Recipe", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (nameField.getText().isEmpty() || ingredientsField.getText().isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                recipe.setName(nameField.getText());
                recipe.setIngredients(ingredientsField.getText());

                tableModel.setValueAt(recipe.getName(), selectedRow, 0);
                tableModel.setValueAt(recipe.getIngredients(), selectedRow, 1);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecipe() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                throw new IllegalArgumentException("No recipe selected.");
            }

            recipes.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecipeManager manager = new RecipeManager();
            manager.setVisible(true);
        });
    }
}
