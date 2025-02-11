package carbonfootprintcalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame implements ActionListener {

    private final JLabel logoLabel;
    private final JButton startButton;

    public LoginScreen() {
        setTitle("TECHGREEN - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Definindo a paleta de cores
        Color backgroundColor = new Color(240, 240, 240); // Cor de fundo
        Color buttonColor = new Color(0, 120, 215); // Azul para o botão
        Color textColor = Color.WHITE; // Cor do texto

        // Painel principal com layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margens
        mainPanel.setBackground(backgroundColor);

        // Painel central para a logo e o botão
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(backgroundColor);

        // Carregando a logo da empresa
        ImageIcon originalLogoIcon = new ImageIcon("src/Icons/Logo.png"); // Caminho correto para a logo
        Image img = originalLogoIcon.getImage();
        Image scaledImg = img.getScaledInstance(300, 195, Image.SCALE_SMOOTH); // Ajusta o tamanho da logo
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImg);

        logoLabel = new JLabel(scaledLogoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza a logo
        centerPanel.add(logoLabel);

        // Espaçamento entre a logo e o botão
        centerPanel.add(Box.createVerticalStrut(20)); // Espaçamento de 20 pixels

        // Botão para iniciar a calculadora
        startButton = new JButton("Iniciar Calculadora");
        startButton.setBackground(buttonColor);
        startButton.setForeground(textColor);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o botão
        startButton.addActionListener(this);
        centerPanel.add(startButton);

        // Adiciona o painel central ao painel principal
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Fecha a tela de login e abre a calculadora
            this.dispose();
            CarbonFootprintCalculator calculator = new CarbonFootprintCalculator();
            calculator.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}

class CarbonFootprintCalculator extends JFrame implements ActionListener {

    private final JLabel titleLabel;
    private final JLabel subtitleLabel;
    private final JLabel questionLabel;
    private final JToggleButton[] options;
    private final ButtonGroup optionGroup;
    private final JButton nextButton;
    private final JButton backButton;

    private final String[] questions = {
        "Qual a frequência de banhos que você toma por dia?",
        "Qual meio de transporte você mais utiliza?",
        "Quantas refeições com carne você consome por semana?",
        "Qual a sua principal fonte de energia em casa?",
        "Quantos eletrônicos você possui em casa?",
        "Quantas horas de ar condicionado você utiliza por dia?"
    };

    private final String[][] optionsText = {
        {"Uma vez por dia", "Duas vezes por dia", "Três vezes por dia", "Mais de três vezes por dia"},
        {"Carro", "Bicicleta", "Transporte público", "A pé"},
        {"Menos de 3", "Entre 3 e 7", "Entre 7 e 14", "Mais de 14"},
        {"Energia elétrica", "Energia solar", "Gás natural", "Lenha"},
        {"Menos de 5", "Entre 5 e 10", "Entre 10 e 15", "Mais de 15"},
        {"Menos de 1", "Entre 1 e 3", "Entre 3 e 5", "Mais de 5"}
    };

    private final double[][] pesos = {
        {1.0, 2.0, 3.0, 4.0}, // Pesos para a frequência de banhos
        {4.0, 1.0, 2.5, 0.5}, // Pesos para o meio de transporte
        {1.0, 2.0, 3.0, 4.0}, // Pesos para o consumo de carne
        {4.0, 0.5, 2.0, 3.0}, // Pesos para a fonte de energia
        {1.0, 2.0, 3.0, 4.0}, // Pesos para o número de eletrônicos
        {1.0, 2.0, 3.0, 4.0}  // Pesos para o uso de ar condicionado
    };

    private int currentQuestion = 0;
    private double totalPeso = 0;

    public CarbonFootprintCalculator() {
        setTitle("TECHGREEN - Calculadora de Carbono");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Definindo a paleta de cores
        Color backgroundColor = new Color(240, 240, 240); // Cor de fundo
        Color questionPanelColor = new Color(0, 150, 136); // Verde para o painel da pergunta
        Color optionColor = new Color(0, 120, 215); // Azul para as opções
        Color selectedOptionColor = new Color(0, 200, 100); // Verde para opção selecionada
        Color textColor = Color.WHITE; // Cor do texto

        // Painel principal com layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margens
        mainPanel.setBackground(backgroundColor);

        // Painel superior com título e subtítulo
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(backgroundColor);
        titleLabel = new JLabel("TECHGREEN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        subtitleLabel = new JLabel("Transformando o futuro", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.BLACK);
        topPanel.add(titleLabel);
        topPanel.add(subtitleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Painel central com a pergunta e opções
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(backgroundColor);

        // Painel da pergunta com fundo verde e bordas arredondadas
        JPanel questionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(questionPanelColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        questionPanel.setOpaque(false);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questionPanel.setPreferredSize(new Dimension(400, 40)); // Altura reduzida
        questionLabel = new JLabel(questions[currentQuestion], SwingConstants.CENTER);
        questionLabel.setFont(new Font("Serif", Font.BOLD, 14));
        questionLabel.setForeground(textColor);
        questionPanel.add(questionLabel);
        centerPanel.add(questionPanel);

        // Espaçamento entre o painel da pergunta e as opções
        centerPanel.add(Box.createVerticalStrut(20)); // Espaçamento de 20 pixels

        optionGroup = new ButtonGroup();
        options = new JToggleButton[4];
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(backgroundColor);
        for (int i = 0; i < options.length; i++) {
            options[i] = new JToggleButton(optionsText[currentQuestion][i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            options[i].setFont(new Font("Serif", Font.BOLD, 12));
            options[i].setBackground(optionColor);
            options[i].setForeground(textColor);
            options[i].setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza as opções
            options[i].setFocusPainted(false);
            options[i].setBorderPainted(false);
            options[i].setContentAreaFilled(false);
            options[i].setPreferredSize(new Dimension(300, 40)); // Tamanho fixo para os botões
            options[i].addActionListener(this);
            optionGroup.add(options[i]);
            optionsPanel.add(options[i]);
            optionsPanel.add(Box.createVerticalStrut(10)); // Espaçamento entre os botões
        }
        centerPanel.add(optionsPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Painel inferior com botões
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(backgroundColor);
        nextButton = new JButton("Próximo");
        nextButton.setBackground(optionColor);
        nextButton.setForeground(textColor);
        nextButton.addActionListener(this);
        backButton = new JButton("Voltar ao início");
        backButton.setBackground(optionColor);
        backButton.setForeground(textColor);
        backButton.addActionListener(this);
        bottomPanel.add(backButton);
        bottomPanel.add(nextButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            // Verifica se o usuário selecionou uma opção
            if (optionGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção antes de prosseguir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Adiciona o peso da resposta atual ao total
            for (int i = 0; i < options.length; i++) {
                if (options[i].isSelected()) {
                    totalPeso += pesos[currentQuestion][i];
                    break;
                }
            }

            if (currentQuestion < questions.length - 1) {
                currentQuestion++;
                updateQuestion();
            } else {
                // Calcula o resultado final
                calculateResult();
            }
        } else if (e.getSource() == backButton) {
            currentQuestion = 0;
            totalPeso = 0; // Reinicia o peso total
            updateQuestion();
        } else {
            // Muda a cor do botão selecionado para verde
            for (JToggleButton option : options) {
                if (option.isSelected()) {
                    option.setBackground(new Color(0, 200, 100));
                } else {
                    option.setBackground(new Color(0, 120, 215));
                }
            }
        }
    }

    private void updateQuestion() {
        questionLabel.setText(questions[currentQuestion]);
        optionGroup.clearSelection();
        for (int i = 0; i < options.length; i++) {
            options[i].setText(optionsText[currentQuestion][i]);
            options[i].setBackground(new Color(0, 120, 215));
        }
    }

    private void calculateResult() {
        // Calcula a média do consumo de carbono
        double media = (totalPeso / 24.0) * 100; // 24 é o peso máximo possível

        // Determina a saúde do gasto de carbono
        String saude;
        String dicas;
        if (media < 30) {
            saude = "Excelente! Você está fazendo um ótimo trabalho em reduzir sua pegada de carbono.";
            dicas = "- Continue utilizando meios de transporte sustentáveis como bicicleta ou caminhada.\n"
                    + "- Mantenha uma dieta equilibrada com pouca carne.\n"
                    + "- Continue utilizando fontes de energia renovável.";
        } else if (media < 60) {
            saude = "Bom! Você está em um bom caminho, mas ainda há espaço para melhorias.";
            dicas = "- Prefira meios de transporte sustentáveis como bicicleta ou caminhada.\n"
                    + "- Reduza o consumo de carne e opte por refeições vegetarianas.\n"
                    + "- Utilize fontes de energia renovável como a energia solar.";
        } else if (media < 90) {
            saude = "Regular. Considere adotar mais práticas sustentáveis para reduzir seu impacto.";
            dicas = "- Reduza o uso de carros e opte por transporte público ou bicicleta.\n"
                    + "- Diminua o consumo de carne e aumente o consumo de vegetais.\n"
                    + "- Desligue aparelhos eletrônicos quando não estiverem em uso.";
        } else {
            saude = "Alto. Parabéns por estar ciente do seu impacto! Vamos trabalhar juntos para melhorar.";
            dicas = "- Considere usar mais transporte público ou bicicleta.\n"
                    + "- Experimente refeições vegetarianas algumas vezes por semana.\n"
                    + "- Desligue aparelhos eletrônicos quando não estiverem em uso.\n"
                    + "- Limite o uso de ar condicionado e mantenha a temperatura em níveis eficientes.";
        }

        // Exibe o resultado final
        String mensagemFinal = "Resultado do Cálculo de Carbono\n\n"
                + "Seu gasto médio de carbono é: " + String.format("%.2f", media) + "%\n\n"
                + saude + "\n\n"
                + "Dicas para reduzir seu consumo de carbono:\n"
                + dicas;

        JOptionPane.showMessageDialog(this, mensagemFinal, "Resultado", JOptionPane.INFORMATION_MESSAGE);

        // Pergunta se o usuário deseja voltar ao início
        int option = JOptionPane.showConfirmDialog(this, "Deseja voltar ao início?", "Voltar", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            currentQuestion = 0;
            totalPeso = 0; // Reinicia o peso total
            updateQuestion();
        } else {
            System.exit(0); // Fecha o programa
        }
    }
}
