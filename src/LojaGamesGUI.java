import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Classe Game
class Game {
    private String nome;
    private String categoria;
    private double preco;

    public Game(String nome, String categoria, double preco) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
    }

    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public double getPreco() { return preco; }

    @Override
    public String toString() {
        return nome + " | Categoria: " + categoria + " | R$ " + preco;
    }
}

// Classe Cliente
class Cliente {
    private String nome;
    private double saldo;
    private ArrayList<Game> jogosComprados;

    public Cliente(String nome, double saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
        this.jogosComprados = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public double getSaldo() { return saldo; }
    public ArrayList<Game> getJogosComprados() { return jogosComprados; }

    public void adicionarSaldo(double valor) throws Exception {
        if (valor <= 0) throw new Exception("Valor inválido para adicionar saldo!");
        this.saldo += valor;
    }

    public void comprarJogo(Game game) throws Exception {
        if (saldo < game.getPreco()) {
            throw new Exception("Saldo insuficiente para comprar este jogo!");
        }
        this.saldo -= game.getPreco();
        jogosComprados.add(game);
    }
}

// Classe principal GUI
public class LojaGamesGUI extends JFrame {
    private DefaultListModel<Game> listaGamesModel;
    private DefaultListModel<Game> listaFiltradaModel;
    private JList<Game> listaGames;
    private Cliente cliente;

    public LojaGamesGUI() {
        super("Loja de Games");
        cliente = new Cliente("Usuário", 200.0); // Cliente inicial com R$200

        // Lista de jogos
        listaGamesModel = new DefaultListModel<>();
        listaFiltradaModel = new DefaultListModel<>();

        // Jogos iniciais
        listaGamesModel.addElement(new Game("The Witcher 3", "RPG", 99.90));
        listaGamesModel.addElement(new Game("CS:GO", "FPS", 49.90));
        listaGamesModel.addElement(new Game("Minecraft", "Sandbox", 89.90));

        listaGames = new JList<>(listaGamesModel);

        // Botões
        JButton btnAddGame = new JButton("Adicionar Game");
        JButton btnAddSaldo = new JButton("Adicionar Saldo");
        JButton btnComprar = new JButton("Comprar Game");
        JButton btnFiltrar = new JButton("Filtrar Games");
        JButton btnDetalhes = new JButton("Exibir Detalhes");
        JButton btnMeusJogos = new JButton("Meus Jogos");

        // Painel
        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.add(btnAddGame);
        panel.add(btnAddSaldo);
        panel.add(btnComprar);
        panel.add(btnFiltrar);
        panel.add(btnDetalhes);
        panel.add(btnMeusJogos);

        add(new JScrollPane(listaGames), BorderLayout.CENTER);
        add(panel, BorderLayout.EAST);

        // Ações dos botões
        btnAddGame.addActionListener(e -> adicionarGame());
        btnAddSaldo.addActionListener(e -> adicionarSaldo());
        btnComprar.addActionListener(e -> comprarGame());
        btnFiltrar.addActionListener(e -> filtrarGames());
        btnDetalhes.addActionListener(e -> exibirDetalhes());
        btnMeusJogos.addActionListener(e -> exibirMeusJogos());

        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Funções
    private void adicionarGame() {
        String nome = JOptionPane.showInputDialog("Nome do game:");
        String categoria = JOptionPane.showInputDialog("Categoria:");
        try {
            double preco = Double.parseDouble(JOptionPane.showInputDialog("Preço:"));
            if (nome == null || nome.isEmpty() || categoria == null || categoria.isEmpty() || preco <= 0) {
                JOptionPane.showMessageDialog(this, "Dados inválidos!");
                return;
            }
            listaGamesModel.addElement(new Game(nome, categoria, preco));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido!");
        }
    }

    private void adicionarSaldo() {
        try {
            double valor = Double.parseDouble(JOptionPane.showInputDialog("Digite o valor a adicionar:"));
            cliente.adicionarSaldo(valor);
            JOptionPane.showMessageDialog(this, "Saldo atualizado: R$ " + cliente.getSaldo());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void comprarGame() {
        Game game = listaGames.getSelectedValue();
        if (game == null) {
            JOptionPane.showMessageDialog(this, "Selecione um game primeiro!");
            return;
        }
        try {
            cliente.comprarJogo(game);
            JOptionPane.showMessageDialog(this, "Game comprado com sucesso! Saldo atual: R$ " + cliente.getSaldo());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void filtrarGames() {
        String filtro = JOptionPane.showInputDialog("Digite categoria ou preço máximo:");
        listaFiltradaModel.clear();
        try {
            double precoMax = Double.parseDouble(filtro);
            for (int i = 0; i < listaGamesModel.size(); i++) {
                if (listaGamesModel.get(i).getPreco() <= precoMax) {
                    listaFiltradaModel.addElement(listaGamesModel.get(i));
                }
            }
        } catch (NumberFormatException e) {
            for (int i = 0; i < listaGamesModel.size(); i++) {
                if (listaGamesModel.get(i).getCategoria().equalsIgnoreCase(filtro)) {
                    listaFiltradaModel.addElement(listaGamesModel.get(i));
                }
            }
        }
        listaGames.setModel(listaFiltradaModel.size() > 0 ? listaFiltradaModel : listaGamesModel);
    }

    private void exibirDetalhes() {
        Game game = listaGames.getSelectedValue();
        if (game == null) {
            JOptionPane.showMessageDialog(this, "Selecione um game!");
            return;
        }
        JOptionPane.showMessageDialog(this, game.toString());
    }

    private void exibirMeusJogos() {
        StringBuilder sb = new StringBuilder("Jogos comprados:\n");
        for (Game g : cliente.getJogosComprados()) {
            sb.append(g.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString() + "\nSaldo atual: R$ " + cliente.getSaldo());
    }

    public static void main(String[] args) {
        new LojaGamesGUI();
    }
}
