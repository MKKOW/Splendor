package gui;

import Controller.BoardMaker;
import Exceptions.InactivePlayersException;
import Model.ClientBoard;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Game extends JPanel {
    private Card[][] cards;
    private Card[] players;
    private JButton Menu;
    private coinStack[] coinStacks;
    private JPanel coinField;
    private int cardHeight;
    private int cardWidth;
    private int Width;
    private int Height;
    private int x;
    private int y;
    private String nick;
    private JButton claimCard;
    private JButton buyCard;
    private JButton getGems;
    private Client client;
    private JSONObject response;
    private JSONObject board;
    private JFrame frame;
    private boolean active;
    private String message;
    private JPanel communication;
    private JLabel communicate;
    private boolean discard;
    private int selfId;
    private int[] temporaryStacks;
    public Game(JFrame frame, JSONObject board, String localNick, Client client) throws IOException, ClassNotFoundException {
        super(null);
        this.frame = frame;
        this.board = board;
        nick = localNick;
        this.client = client;
        message = "";
        setBackground(Color.BLACK);
        frame.add(this);
        loadboard();

    }
    class boardListener implements Runnable{
        public boolean done;
        public boardListener(){
            super();
            done = false;
        }
        @Override
        public void run() {
            try {
                board = client.getCurrentBoard();
                clear();
                loadboard();
                done=true;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    void makePlayer(int id,JSONObject player) throws IOException, ClassNotFoundException {
        int[] cost=new int[6];
        int[] discount= new int[6];
        JSONObject developmentCards=player.getJSONObject("developmentCards");
        String[] ids=JSONObject.getNames(developmentCards);
        JSONObject cash=player.getJSONObject("cash");
        cost[0]+=cash.getInt("white");
        cost[1]+=cash.getInt("blue");
        cost[2]+=cash.getInt("green");
        cost[3]+=cash.getInt("red");
        cost[4]+=cash.getInt("black");
        cost[5]+=cash.getInt("yellow");
        if(ids!=null) {
            for (String i : ids) {
                JSONObject card = developmentCards.getJSONObject(i);
                JSONObject discounts = card.getJSONObject("discount");
                discount[0] += discounts.getInt("white");
                discount[1] += discounts.getInt("blue");
                discount[2] += discounts.getInt("green");
                discount[3] += discounts.getInt("red");
                discount[4] += discounts.getInt("black");
                discount[5] = 0;
            }
        }
        int points=0;
        players[id]=new Card(-1,cost[0],cost[1],cost[2],cost[3],cost[4],cost[5],discount[0],discount[1],discount[2],discount[3],discount[4],discount[5],points,true,cardWidth,cardHeight,Width-cardWidth,Height-cardHeight,player.getString("nick"));
        add(players[id]);
        players[id].forceVisible();
        JSONObject claimedCardJSON = null;
        try {//Nothing else worked
            claimedCardJSON = player.getJSONObject("claimedCard");
        }
        catch (Exception ignored){

        }
        Card claimedCard = null;
        switch (id) {
            case 0:
                players[id].setLocation(x, y+cardHeight*4);
                players[id].setSize(cardWidth*5,cardHeight);
                if(claimedCardJSON!=null){
                    claimedCard=new Card(claimedCardJSON,true,cardWidth,cardHeight,cardWidth,0,"Claimed Card");
                    setCardBackground(claimedCard,claimedCardJSON);
                    players[id].add(claimedCard);
                }
                break;
            case 1:
                players[id].setLocation(Width-2*cardWidth, y);
                players[id].setSize(cardWidth,cardHeight*4);
                if(claimedCardJSON!=null){
                    claimedCard=new Card(claimedCardJSON,true,cardWidth,cardHeight,0,cardHeight,"Claimed Card");
                    setCardBackground(claimedCard,claimedCardJSON);
                    players[id].add(claimedCard);
                }
                break;
            case 2:
                players[id].setLocation(x, 0);
                players[id].setSize(cardWidth*5,cardHeight);
                if(claimedCardJSON!=null){
                    claimedCard=new Card(claimedCardJSON,true,cardWidth,cardHeight,cardWidth,0,"Claimed Card");
                    setCardBackground(claimedCard,claimedCardJSON);
                    players[id].add(claimedCard);
                }
                break;
            case 3:
                players[id].setLocation(0, y);
                players[id].setSize(cardWidth,cardHeight*4);
                if(claimedCardJSON!=null){
                    claimedCard=new Card(claimedCardJSON,true,cardWidth,cardHeight,0,cardHeight,"Claimed Card");
                    setCardBackground(claimedCard,claimedCardJSON);
                    players[id].add(claimedCard);
                }
                break;
        }
        if(nick.equals(player.getString("nick"))) {
            selfId=id;
            if(player.getBoolean("active")) {
                active = true;
                addButtons(claimedCard);
            } else
                active = false;
            if(discard) {
                int[] table = new int[5];
                for(int i=0;i<5;i++){
                    coinStack stack=players[id].getCosts()[i];
                    int finalI = i;
                    stack.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                                table[finalI]++;
                                stack.setAmount(stack.getAmount()-1);
                        }
                    });
                }
                JButton discard = new JButton("Discard");
                discard.setLocation(x+5*cardWidth,y+cardHeight*4);
                discard.setSize(Width-(x+5*cardWidth),cardHeight);
                add(discard);
                discard.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            parseDiscardGem(table);
                        } catch (IOException | ClassNotFoundException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    private void parseDiscardGem(int[] table) throws IOException, ClassNotFoundException {
        JSONObject request=new JSONObject();
        request.put("request_type","discard_gems");
        request.put("white",table[0]);
        request.put("blue",table[1]);
        request.put("green",table[2]);
        request.put("red",table[3]);
        request.put("black",table[4]);
        System.out.println(request);
        response=client.takeRequest(request);
        System.out.println(response);
        verifyMove();
    }

    private void setCardBackground(Card claimedCard, JSONObject claimedCardJSON) {
        String level = claimedCardJSON.getString("level");
        switch (level) {
            case "One":
                claimedCard.setBackground(Color.BLUE);
                break;
            case "Two":
                claimedCard.setBackground(Color.YELLOW);
                break;
            case "Three":
                claimedCard.setBackground(Color.GREEN);
                break;
        }
    }

    private void addButtons(Card claimedCard){
            buyCard = new JButton("Buy card");
            claimCard = new JButton("Claim card");
            getGems = new JButton("Get gems");
            buyCard.setLocation(0,y+cardHeight*4);
            claimCard.setLocation(x/3,y+cardHeight*4);
            getGems.setLocation(x*2/3,y+cardHeight*4);
            buyCard.setSize(x/3,cardHeight);
            claimCard.setSize(x/3,cardHeight);
            getGems.setSize(x/3,cardHeight);
            add(buyCard);
            add(claimCard);
            add(getGems);
            buyCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i= 1; i<=3;i++){
                    for(int j=1;j<=4;j++){
                        Card card=cards[i][j];
                        if(card!=null){
                            card.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    for(int i2= 1; i2<=3;i2++) {
                                        for (int j2 = 1; j2 <= 4; j2++) {
                                            Card card2=cards[i2][j2];
                                            card2.removeMouseListener(card2.getMouseListeners()[0]);
                                        }
                                    }
                                    if(claimedCard!= null)
                                        claimedCard.removeMouseListener(claimedCard.getMouseListeners()[0]);
                                        try {
                                            parseBuy(card.getId(), "table");
                                        } catch (IOException | ClassNotFoundException ioException) {
                                            ioException.printStackTrace();
                                        }
                                }
                            });
                        }
                    }
                }
                if(claimedCard!=null){
                    claimedCard.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            for(int i2= 1; i2<=3;i2++) {
                                for (int j2 = 1; j2 <= 4; j2++) {
                                    Card card2=cards[i2][j2];
                                    card2.removeMouseListener(card2.getMouseListeners()[0]);
                                }
                            }
                            try {
                                parseBuy(claimedCard.getId(),"hand");
                            } catch (IOException | ClassNotFoundException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        claimCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i= 0; i<=3;i++){
                    for(int j= 0;j<=4;j++){
                        Card card=cards[i][j];
                        if(card!=null){
                            card.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    for(int i2= 1; i2<=3;i2++) {
                                        for (int j2 = 1; j2 <= 4; j2++) {
                                            Card card2=cards[i2][j2];
                                            if(card2!=null)
                                                card2.removeMouseListener(card2.getMouseListeners()[0]);
                                        }
                                    }
                                    try {
                                        parseClaim(card.getId());
                                    } catch (IOException | ClassNotFoundException ioException) {
                                        ioException.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
        getGems.addActionListener(new ActionListener() {
            int[] table = new int[5];
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<5;i++){
                    coinStack stack=coinStacks[i];
                    int finalI = i;
                    stack.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if(checkTable(table,finalI)){
                                table[finalI]++;
                                stack.setAmount(stack.getAmount()-1);
                                players[selfId].getCosts()[finalI].setAmount(players[selfId].getCosts()[finalI].getAmount()+1);
                                if(checkFinish(table)) {
                                    for(int i=0;i<5;i++) {
                                        coinStack stack = coinStacks[i];
                                        stack.removeMouseListener(stack.getMouseListeners()[0]);
                                    }
                                    try {
                                        parseGetGem(table);
                                    } catch (IOException | ClassNotFoundException ioException) {
                                        ioException.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    private boolean checkFinish(int[] table) {
        int count_single=0;
        for(int i=0;i<table.length;i++){
            if(table[i]==2)
                return true;
            if(table[i]==1)
                count_single++;
        }
        if(count_single==3){
            return true;
        }
        return false;
    }

    private boolean checkTable(int[] table, int finalI) {
        if(table[finalI]==1) {
            for (int i = 0; i < table.length; i++) {
                if(i!=finalI&&table[i]==1){
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }

    private void clear() {
        removeAll();
    }

    private void loadboard() throws IOException, ClassNotFoundException {
        cards=new Card[4][5];
        players=new Card[4];
        Menu=new JButton("Menu");
        cardHeight=frame.getHeight()/7;
        cardWidth=cardHeight*57/88;
        Width=frame.getWidth();
        System.out.println(Width);
        Height=frame.getHeight();
        System.out.println(Height);
        x=(Width-cardWidth*5)/2;
        y=(Height-cardHeight*4)/2;
        Menu.setLocation(0,0);
        Menu.setSize(Height/12,Height/12);
        add(Menu);
        coinStacks=new coinStack[6];
        coinField=new JPanel(null);
        JSONObject cash=board.getJSONObject("bankCash");

        for(int i=0;i<6;i++) {
            coinStacks[i] = new coinStack();
            coinField.add(coinStacks[i]);
            coinStacks[i].setLocation(0,i*cardHeight/6);
            coinStacks[i].setSize(cardHeight/6,cardHeight/6);
        }
        add(coinField);
        coinField.setLocation(x-cardHeight/6,y);
        coinField.setSize(cardHeight/6,cardHeight);
        coinStacks[0].set(cash.getInt("white"),"black",Color.WHITE);
        coinStacks[1].set(cash.getInt("blue"),"white",Color.BLUE);
        coinStacks[2].set(cash.getInt("green"),"black",Color.GREEN);
        coinStacks[3].set(cash.getInt("red"),"black",Color.RED);
        coinStacks[4].set(cash.getInt("black"),"white",Color.BLACK);
        coinStacks[5].set(cash.getInt("yellow"),"black",Color.YELLOW);
        JSONObject playersOnBoard=board.getJSONObject("players");
        String[] nicks=JSONObject.getNames(playersOnBoard);
        int id=0;
        for(String nick:nicks){
            System.out.println(nick);
            System.out.println(id);
            makePlayer(id,playersOnBoard.getJSONObject(nick));
            id++;
        }
        for(int i=0;i<4;i++){
            JSONArray row;
            if(i==0)
                row=board.getJSONObject("nobles").getJSONArray("nobles");
            else
                row=board.getJSONObject("developmentCardsOnBoard").getJSONArray("level"+i);
            for(int j=0;j<row.length()+(i!=0?1:0);j++){
                if(i!=0&&j==0){
                    cards[i][j]=new Card(-1,0,0,0,0,0,0,0,0,0,0,0,0,0,true,cardWidth,cardHeight,x+cardWidth*j,y+cardHeight*i,"");
                }
                else{
                    JSONObject card= row.getJSONObject(i!=0?j-1:j);
                    cards[i][j]=new Card(card,i==0,cardWidth,cardHeight,x+cardWidth*j,y+cardHeight*i,"");
                }
            }
        }
        cards[1][0].setVisible(true);
        cards[2][0].setVisible(true);
        cards[3][0].setVisible(true);
        for(Card card:cards[0]){
            if(card!=null) {
                card.setBackground(Color.RED);
                add(card);
            }
        }
        for(Card card:cards[1]) {
            if(card!=null) {
                card.setBackground(Color.BLUE);
                add(card);
            }
        }
        for(Card card:cards[2]) {
            if(card!=null) {
                card.setBackground(Color.YELLOW);
                add(card);
            }
        }
        for(Card card:cards[3]) {
            if(card!=null) {
                card.setBackground(Color.GREEN);
                add(card);
            }
        }
        JFrame finalFrame = frame;
        Menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                finalFrame.setContentPane(new Menu(finalFrame).view);
            }
        });
        System.out.println(board);
        communication = new JPanel(null);
        communicate = new JLabel(message);
        communicate.setLocation(0,0);
        communicate.setText(message);
        communication.setLocation(x+5*cardWidth,0);
        communication.setSize(Width-x-5*cardWidth,y);
        communicate.setSize(Width-x-5*cardWidth,y);
        communication.setBackground(Color.LIGHT_GRAY);

        add(communication);
        communication.add(communicate);
        revalidate();
        repaint();
        if(!active) {
            boardListener boardListener = new boardListener();
            new Thread(boardListener).start();
        }
    }
    private void parseGetGem(int[] table) throws IOException, ClassNotFoundException {
        JSONObject request=new JSONObject();
        request.put("request_type","get_gems");
        request.put("white",table[0]);
        request.put("blue",table[1]);
        request.put("green",table[2]);
        request.put("red",table[3]);
        request.put("black",table[4]);
        System.out.println(request);
        response=client.takeRequest(request);
        System.out.println(response);
        verifyMove();
    }
    private void parseClaim(int id) throws IOException, ClassNotFoundException {
        JSONObject request=new JSONObject();
        request.put("request_type","claim_card");
        request.put("card_id",id);
        System.out.println(request);
        response=client.takeRequest(request);
        System.out.println(response);
        verifyMove();
    }

    private void parseBuy(int id,String place) throws IOException, ClassNotFoundException {
        JSONObject request=new JSONObject();
        request.put("request_type","buy_card");
        request.put("place",place);
        request.put("card_id",id);
        System.out.println(request);
        response=client.takeRequest(request);
        System.out.println(response);
        verifyMove();
    }
    private void verifyMove() throws IOException, ClassNotFoundException {
        if(response.getString("answer_type").equals("move_verification")) {
            if (response.getString("result").equals("ok")) {
                response = client.getResponse();
                System.out.println(response);
                if(response.getString("answer_type").equals("end_of_round")) {
                    endOfRound();
                }
            }
            else{
                message = "Illegal move";
                clear();
                loadboard();
            }
        }else if(response.getString("answer_type").equals("end_of_round")){
                endOfRound();
        }
        else{
            System.out.println("Something went wrong:"+response);
        }
    }
    private void endOfRound() throws IOException, ClassNotFoundException {
        if (response.getString("result").equals("ok")) {
            board = client.getCurrentBoard();
            message = "Ok";
            discard = false;
            clear();
            loadboard();
        } else if(response.getString("result").equals("discard_gems")) {
            message = "You have to discard some of your gems";
            discard=true;
            temporaryStacks = new int[5];
            for (int i=0;i<5;i++){
                temporaryStacks[i] = players[selfId].getCosts()[i].getAmount();
            }
            clear();
            loadboard();
            for (int i=0;i<5;i++) {
                players[selfId].getCosts()[i].setAmount(temporaryStacks[i]);
            }
        }
    }
}
