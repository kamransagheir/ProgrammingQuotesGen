package com.demo.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * The front controller for the class
 *
 */
@Controller
public class AppController {

	// instance of the APICaller service
	@Autowired
	private APICaller apiCaller;

	/**
	 * controller method for initial display, when the web app spins up
	 * 
	 * @param model the MVC model
	 * @return html page with a random quote and author
	 */
	@GetMapping("/")
	public String home(Model model) {
		// System.out.println("inside home");
		String content = apiCaller.content.toString();
		String auth = apiCaller.auth.toString();
		System.out.println("heere." + content);

		model.addAttribute("quote", content);
		model.addAttribute("author", auth);
		return "index.html";
	}

	/**
	 * controller method for all subsequent calls
	 * 
	 * @param model the MVC model
	 * @return html page with new random quote and author
	 */
	@GetMapping("/new")
	public String newQuote(Model model) {
		// System.out.println("inside newQuote");
		apiCaller.newCall(null);

		String content = apiCaller.content.toString();
		String auth = apiCaller.auth.toString();

		model.addAttribute("quote", content);
		model.addAttribute("author", auth);
		return "index.html";
	}

	/**
	 * controller method for getting similar quotes when user gives four or five
	 * star rating to a quote
	 * 
	 * @param model the MVC model
	 * @return html page with a new quote that is similar to the previous one and
	 *         the author name
	 */
	@GetMapping("/showSimilar")
	public String getSimilarQuote(Model model) {
		System.out.println("here before 101010" + apiCaller.content);
		System.out.println("inside getSimilarQuote: " + apiCaller.tags);

		// call the quotes api with previously liked quote tags to get a similar quote
		apiCaller.newCall(apiCaller.tags.toString());

		// new quote
		String content = apiCaller.content.toString();
		// new quote author
		String auth = apiCaller.auth.toString();
		System.out.println("here 101010" + content);

		model.addAttribute("quote", content);
		model.addAttribute("author", auth);
		return "index.html";
	}

}
