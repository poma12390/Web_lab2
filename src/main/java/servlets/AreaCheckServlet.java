package servlets;


import data.Result;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AreaCheckServlet", value = "/checkArea")
public class AreaCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();

            session.setAttribute("validity", "false");
            boolean validate = validateAll(req.getParameter("x"), req.getParameter("y"), req.getParameter("r"));
            if (validate) {
                long startTime = System.nanoTime();
                double x = Double.parseDouble(req.getParameter("y"));
                double y = Double.parseDouble(req.getParameter("x"));
                double r = Double.parseDouble(req.getParameter("r"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                String currentTime = formatter.format(date);
                boolean hit = (y <= r && y >= 0 && x >= 0 && x <= r / 2) || (x <= 0 && x >= y - r / 2 && y <= r / 2 && y >= 0) || (x >= 0 && y <= 0 && x * x + y * y <= r * r / 4);
                Result result = new Result(Math.round(x * 100) / 100D, Math.round(y * 100) / 100D, r, currentTime, System.nanoTime() - startTime, hit);
                List<Result> results;
                if (session.getAttribute("results") == null) {
                    results = new ArrayList<>();
                } else {
                    results = (ArrayList<Result>) session.getAttribute("results");
                }
                results.add(result);
                session.setAttribute("results", results);
                session.setAttribute("validity", "true");

                getServletContext().getRequestDispatcher("/table.jsp").forward(req, resp);
            } else {
                throw new IllegalArgumentException("Неверные значения параметров!");
            }
        } catch (IllegalArgumentException|IOException e){
            resp.setStatus(400);
            req.setAttribute("message", "-> Ошибка: " + e.getMessage());
        }finally {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }

    }


    private boolean validateX(String xString) {
        try {
            List<Double> rRange = new ArrayList<>(Arrays.asList(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5,1.0, 1.5, 2.0));

            double rValue = Double.parseDouble(xString);
            return rRange.contains(rValue);
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean validateY(String yString) {
        try {
            double yValue = Double.parseDouble(yString);
            return yValue >= -3 && yValue <= 5;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean validateR(String rString) {
        try {
            List<Double> rRange = new ArrayList<>(Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0));

            double rValue = Double.parseDouble(rString);
            return rRange.contains(rValue);
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean validateAll(String xString, String yString, String rString) {
        return validateX(yString) && validateY(xString) && validateR(rString);
    }

    private boolean checkHit(double x, double y, int r) {
        return (x <= 0 && y <= 0 && y >= -x / 2 - (double) r / 2) ||
                (x <= 0 && y >= 0 && x >= -(double) r / 2 && y <= r) ||
                (x >= 0 && y <= 0 && Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2));
    }
}