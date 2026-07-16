/**
 * Minimalist copy of
 * https://mujs.org
 * There is no license on vibe code, so this is public domain.
 *
 * @file		mu.js
 * @license		public-domain
 *
 *
 * ============================================================================
 * TABLE OF CONTENTS
 * ============================================================================
 *
 * 1. Overview
 * 2. Installation
 * 3. Basic usage
 * 4. Configuration reference
 * 5. Events
 * 6. View Transitions
 * 7. Forms
 * 8. History & Scroll
 * 9. Progress bar
 * 10. Programmatic usage
 *
 *
 * ============================================================================
 * 1. OVERVIEW
 * ============================================================================
 *
 * µJS intercepts clicks on links and form submissions to load pages via AJAX
 * instead of full browser navigation. The fetched content replaces the current 
 * page, demonstrating that the whole web 2.0 thing was basically a party trick.
 *
 * ============================================================================
 * 2. INSTALLATION
 * ============================================================================
 *
 * Include the scripts at the end of <body>:
 *
 *   <script src="/path/to/mu.js"></script>
 *   <script>mu.init();</script>
 *
 * ============================================================================
 * 3. BASIC USAGE
 * ============================================================================
 *
 * After calling mu.init(), all internal links (href starting with "/") are
 * automatically intercepted. Clicking a link fetches the page via AJAX and
 * replaces the current <body> with the fetched <body>.
 *
 * No modification of your HTML is needed for basic usage.
 *
 * ============================================================================
 * 4. CONFIGURATION REFERENCE
 * ============================================================================
 *
 * Pass an object to mu.init() to override defaults:
 *
 *   mu.init({
 *       history: false,
 *       processForms: false
 *   });
 *
 *   processLinks      (bool)    Process <a> tags. Default: true.
 *   processForms      (bool)    Process <form> tags. Default: true.
 *   history           (bool)    Add URL to browser history. Default: true.
 *   title             (string)  Title selector. Default: "title".
 *   scroll            (bool)    Force scroll behavior. Default: null (auto).
 *   progress          (bool)    Show progress bar. Default: true.
 *   transition        (bool)    Enable View Transitions. Default: true.
 *
 *
 * ============================================================================
 * 5. EVENTS
 * ============================================================================
 *
 * µJS dispatches CustomEvents on `document`. All events carry a `detail`
 * object with `lastUrl` and `previousUrl`.
 *
 *   mu:init           Fired after initialization.
 *                     detail: {url}
 *
 *   mu:before-fetch   Fired before fetching. Cancelable (preventDefault()
 *                     aborts the load).
 *                     detail: {url, fetchUrl, config, sourceElement}
 *
 *   mu:before-render  Fired after fetch, before DOM injection. Cancelable.
 *                     detail.html can be modified to alter injected content.
 *                     detail: {url, finalUrl, html, config}
 *
 *   mu:after-render   Fired after DOM injection.
 *                     detail: {url, finalUrl, mode}
 *
 *   mu:fetch-error    Fired on fetch failure or HTTP error.
 *                     detail: {url, fetchUrl, status, response, error}
 *
 * Example:
 *
 *   document.addEventListener("mu:after-render", function(e) {
 *       console.log("Loaded: " + e.detail.url);
 *       myApp.initWidgets();
 *   });
 *
 *
 * ============================================================================
 * 6. VIEW TRANSITIONS
 * ============================================================================
 *
 * µJS uses the View Transitions API (document.startViewTransition) when
 * supported by the browser. This provides smooth animated transitions between
 * page states.
 *
 * Enabled by default. Falls back silently on unsupported browsers.
 *
 * Disable globally:
 *
 *   mu.init({ transition: false });
 *
 * ============================================================================
 * 7. FORMS
 * ============================================================================
 *
 * µJS intercepts form submissions. HTML5 validation (reportValidity) is
 * checked before any fetch.
 *
 * GET forms: data is serialized as query string, behaves like a link.
 * POST forms: data is sent as URL-encoded (default) or FormData
 * (when enctype="multipart/form-data", e.g. file uploads).
 * History is disabled by default
 * (POST responses should not be replayed via browser back button).
 *
 * ============================================================================
 * 8. HISTORY & SCROLL
 * ============================================================================
 *
 * Defaults depend on the mode and context:
 *   - GET link/form: history=true, scroll=true.
 *   - POST/PUT/PATCH/DELETE form: history=false, scroll=true.
 *
 * Disable history globally:
 *
 *   mu.init({ history: false });
 *
 * ============================================================================
 * 9. PROGRESS BAR
 * ============================================================================
 *
 * A thin progress bar is displayed at the top of the page during fetch.
 * It is styled with inline CSS (no external stylesheet needed).
 *
 * The bar element has id="mu-progress" and can be styled via CSS:
 *
 *   #mu-progress { background: red !important; height: 5px !important; }
 *
 * Disable globally:
 *
 *   mu.init({ progress: false });
 *
 * ============================================================================
 * 10. PROGRAMMATIC USAGE
 * ============================================================================
 *
 *   // Load a page
 *   mu.load("/page", { history: false, target: "#content" });
 *
 *   // Get last loaded URL
 *   mu.getLastUrl();
 *
 *   // Get previous URL
 *   mu.getPreviousUrl();
 *
 */
var mu = mu || new function() {

	/* ********** DEFAULT CONFIGURATION ********** */
	/**
	 * Default configuration values.
	 * Overridden by the object passed to mu.init().
	 * @type {Object}
	 */
	this._defaults = {
		/** @type {boolean} Process <a> tags. */
		processLinks: true,
		/** @type {boolean} Process <form> tags. */
		processForms: true,
		/** @type {boolean} Add URL to browser history. */
		history: true,
		/** @type {string} Title selector ("selector" or "selector/attribute"). */
		title: "title",
		/** @type {boolean|null} Scroll to top. null = auto (true). */
		scroll: null,
		/** @type {string|null} Prefix added to fetched URLs. */
		urlPrefix: null,
		/** @type {boolean} Show progress bar during fetch. */
		progress: true,
		/** @type {boolean} Enable View Transitions API. */
		transition: true
	};

	/* ********** INTERNAL STATE ********** */
	/** @type {Object} Active configuration (defaults merged with init params). */
	this._cfg = {};
	/** @type {string|null} Last URL loaded by µJS. */
	this._lastUrl = null;
	/** @type {string|null} URL before the last one. */
	this._previousUrl = null;
	/** @type {AbortController|null} Controller for the current in-flight request. */
	this._abortCtrl = null;
	/** @type {Element|null} Progress bar DOM element. */
	this._progressEl = null;
	/** @type {Object} Map of already-loaded external script URLs. */
	this._jsIncludes = {};
	/** @type {boolean} True after first init() call. Prevents duplicate listeners. */
	this._initialized = false;

	/* ********** INITIALIZATION ********** */
	/**
	 * Initialize µJS.
	 * Sets up event delegation and merges configuration.
	 * Should be called once when the page loads.
	 * @param	{Object}	[params]	Configuration overrides (see _defaults).
	 */
	this.init = function(params) {
		mu._cfg = Object.assign({}, mu._defaults, params || {});
		// Parse title selector (supports "selector/attribute" syntax)
		mu._cfg._titleAttr = null;
		if (mu._cfg.title) {
			var pos = mu._cfg.title.lastIndexOf("/");
			if (pos !== -1) {
				mu._cfg._titleAttr = mu._cfg.title.substring(pos + 1);
				mu._cfg.title = mu._cfg.title.substring(0, pos);
			}
		}
		// Pre-populate _jsIncludes with scripts already on the page.
		// Prevents _runScripts from re-downloading them on navigation.
		var existingScripts = document.querySelectorAll("script[src]");
		for (var i = 0; i < existingScripts.length; i++)
			mu._jsIncludes[existingScripts[i].getAttribute("src")] = true;
		// Event delegation (attached once, works for all current and future elements)
		if (!mu._initialized) {
			document.addEventListener("click", mu._onClick);
			document.addEventListener("submit", mu._onSubmit);
			window.addEventListener("popstate", mu._onPopState);
			mu._initialized = true;
		}
		// Save initial history state
		window.history.replaceState({mu: true, url: location.pathname + location.search}, "");
		// Emit init event
		mu._emit("mu:init", {url: location.pathname + location.search});
	};

	/* ********** PUBLIC API ********** */
	/**
	 * Programmatic page load.
	 * @param	{string}	url	URL to load.
	 * @param	{Object}	[opts]	Per-load configuration overrides.
	 */
	this.load = function(url, opts) {
		var cfg = Object.assign({}, mu._cfg, opts || {});
		mu._loadExec(mu._resolveUrl(url) || url, cfg);
	};
	/**
	 * Get the last URL loaded by µJS.
	 * @return	{string|null}	The last URL, or null if µJS hasn't loaded a page yet.
	 */
	this.getLastUrl = function() {
		return (mu._lastUrl);
	};
	/**
	 * Get the URL before the last one.
	 * @return	{string|null}	The previous URL, or null.
	 */
	this.getPreviousUrl = function() {
		return (mu._previousUrl);
	};

	/* ********** URL RESOLUTION ********** */
	/**
	 * Resolve a URL to a local path.
	 * Handles relative URLs (../page.html, page.html), absolute same-origin URLs
	 * (https://same-domain.com/page), and local paths (/page).
	 * Returns null for external URLs, hash-only links, and invalid URLs.
	 * @param	{string}	url	Raw URL from href, action, or mu-url attribute.
	 * @return	{string|null}	Local path (e.g. "/page") or null if external/invalid.
	 */
	this._resolveUrl = function(url) {
		// Skip empty URLs and hash-only URLs
		if (!url || url.charAt(0) === "#")
			return (null);
		// Fast path: already a local path (starts with "/" but not "//")
		if (url.charAt(0) === "/" && url.charAt(1) !== "/")
			return (url);
		// Resolve relative or absolute URLs
		try {
			var parsed = new URL(url, window.location.href);
			if (parsed.origin === window.location.origin)
				return (parsed.pathname + parsed.search + parsed.hash);
		} catch(e) {}
		return (null);
	};

	/* ********** ELEMENT FILTERING ********** */
	/**
	 * Determine if an element should be intercepted by µJS.
	 * Checks for target/download attributes, onclick/onsubmit handlers,
	 * and URL (must be same-origin: local paths, relative URLs, or absolute same-domain).
	 * @param	{Element}	el	DOM element (<a> or <form>).
	 * @return	{boolean}	True if µJS should handle this element.
	 */
	this._shouldProcess = function(el) {
		// Has target attribute (opens in new window/frame)
		if (el.hasAttribute("target"))
			return (false);
		// Has download attribute (triggers file download)
		if (el.hasAttribute("download"))
			return (false);
		// Links: skip if has onclick handler
		if (el.tagName.toUpperCase() === "A" && el.hasAttribute("onclick"))
			return (false);
		// Forms: skip if has onsubmit handler
		if (el.tagName.toUpperCase() === "FORM" && el.hasAttribute("onsubmit"))
			return (false);
		// Check URL: must be same-origin (local path, relative, or absolute same-domain)
		var url = el.getAttribute("href") || el.getAttribute("action") || "";
		return (mu._resolveUrl(url) !== null);
	};

	/* ********** EVENT HANDLERS ********** */
	/**
	 * Click handler (event delegation on document).
	 * Intercepts clicks on <a> elements that pass _shouldProcess().
	 * Modifier keys (ctrl, meta, shift, alt) are ignored to allow
	 * native browser behavior (open in new tab, etc.).
	 * @param	{MouseEvent}	e	Click event.
	 */
	this._onClick = function(e) {
		var el = e.target.closest("a");
		if (!el || !mu._shouldProcess(el))
			return;
		// Links: check processLinks config
		if (el.tagName.toUpperCase() === "A" && !mu._cfg.processLinks)
			return;
		// Modifier keys: let browser handle (new tab, etc.)
		if (e.ctrlKey || e.metaKey || e.shiftKey || e.altKey)
			return;
		var cfg = Object.assign({}, mu._cfg);
		var url = mu._resolveUrl(el.getAttribute("href"));
		if (!url)
			return;
		e.preventDefault();
		cfg._sourceElement = el;
		// Default to GET if no method specified
		if (!cfg.method)
			cfg.method = "get";
		mu._loadExec(url, cfg);
	};
	/**
	 * Form submit handler (event delegation on document).
	 * Intercepts submissions of <form> elements that pass _shouldProcess().
	 * Runs HTML5 validation and optional custom validation before fetching.
	 * GET forms: data serialized as query string.
	 * POST forms: data sent as URL-encoded (or FormData for file uploads),
	 * history disabled by default.
	 * @param	{SubmitEvent}	e	Submit event.
	 */
	this._onSubmit = function(e) {
		var form = e.target.closest("form");
		if (!form || !mu._cfg.processForms || !mu._shouldProcess(form))
			return;
		// HTML5 validation
		if (!form.reportValidity())
			return;
		var method = (form.getAttribute("method") || "get").toLowerCase();
		var cfg = Object.assign({}, mu._cfg);
		cfg.method = method;
		var url = mu._resolveUrl(form.getAttribute("action"));
		if (!url)
			return;
		cfg._sourceElement = form;
		e.preventDefault();
		var formData = new FormData(form, e.submitter);
		if (method === "get") {
			var qs = new URLSearchParams(formData).toString();
			url = url + "?" + qs;
		} else {
			cfg.postData = (form.enctype === "multipart/form-data")
			               ? formData
			               : new URLSearchParams(formData);
			// Non-GET forms: no history by default, scroll to top in navigation mode
			cfg.history = false;
			cfg.scroll = true;
		}
		mu._loadExec(url, cfg);
	};
	/**
	 * Popstate handler (browser back/forward buttons).
	 * Reloads the page from the stored history state.
	 * If no µJS state is found, falls back to native browser navigation.
	 * Restores scroll position after rendering if available in state.
	 * @param	{PopStateEvent}	e	PopState event.
	 */
	this._onPopState = function(e) {
		var state = e.state;
		if (!state || !state.mu)
			return;
		var cfg = Object.assign({}, mu._cfg);
		cfg.history = false;
		cfg.scroll = false; // don't auto-scroll, we restore manually
		cfg._popstate = true; // skip _saveScroll (wrong history entry during popstate)
		cfg._restoreScroll = state.scrollX !== undefined ? {x: state.scrollX, y: state.scrollY} : null;
		mu._loadExec(state.url, cfg);
	};

	/* ********** CORE LOADING ********** */
	/**
	 * Fetch a URL and render it into the page.
	 * Handles abort of in-flight requests, progress bar,
	 * view transitions, history management, and scroll behavior.
	 * @param	{string}	url	URL to fetch.
	 * @param	{Object}	cfg	Effective configuration.
	 */
	this._loadExec = async function(url, cfg) {
		var fetchUrl = cfg.urlPrefix ? cfg.urlPrefix + url : url;
		// Save current scroll position (skip for trigger loads and popstate)
		if (!cfg._trigger && !cfg._popstate)
			mu._saveScroll();
		// Emit before-fetch event (cancelable)
		if (!mu._emit("mu:before-fetch", {url: url, fetchUrl: fetchUrl, config: cfg, sourceElement: cfg._sourceElement || null}))
			return;
		// Abort controller: trigger loads use a local controller to avoid
		// interfering with the global navigation abort controller.
		var abortCtrl;
		if (cfg._trigger) {
			abortCtrl = new AbortController();
		} else {
			if (mu._abortCtrl)
				mu._abortCtrl.abort();
			abortCtrl = mu._abortCtrl = new AbortController();
		}
		if (!cfg._trigger)
			mu._showProgress();
		try {
			var html = null;
			var resp = null;
			var finalUrl = url;
			if (!html) {
				// Build fetch options
				var fetchOpts = {
					signal: abortCtrl.signal,
					headers: {
						"X-Requested-With": "XMLHttpRequest"
					}
				};
				var httpMethod = (cfg.method || "get").toUpperCase();
				if (httpMethod !== "GET") {
					fetchOpts.method = httpMethod;
					fetchOpts.headers["X-Mu-Method"] = httpMethod;
					if (cfg.postData)
						fetchOpts.body = cfg.postData;
				}
				resp = await fetch(fetchUrl, fetchOpts);
				if (!resp.ok) {
					mu._emit("mu:fetch-error", {url: url, fetchUrl: fetchUrl, status: resp.status, response: resp});
					return;
				}
				if (resp.redirected)
					finalUrl = new URL(resp.url).pathname + new URL(resp.url).search;
				html = await resp.text();
			}
			// Emit before-render (cancelable, allows html modification)
			var detail = {url: url, finalUrl: finalUrl, html: html, config: cfg};
			if (!mu._emit("mu:before-render", detail))
				return;
			// Compute effective history (used for pushState and title update)
			cfg._addHistory = cfg.history;
			if (resp && resp.redirected)
				cfg._addHistory = true;
			// Post-render tasks: history, scroll, event emission.
			// Grouped in a function so it runs after the DOM update regardless
			// of whether View Transitions are used (async) or not (sync).
			var postRender = function() {
				// History management
				mu._previousUrl = mu._lastUrl;
				mu._lastUrl = finalUrl;
				if (cfg._addHistory)
					window.history.pushState({mu: true, url: finalUrl}, "", finalUrl);
				// Scroll management
				if (cfg._restoreScroll) {
					// Restore scroll position from history state (back/forward)
					window.scrollTo(cfg._restoreScroll.x, cfg._restoreScroll.y);
				} else {
					var shouldScroll = cfg.scroll !== null ? cfg.scroll : true;
					if (shouldScroll) {
						var hashIdx = url.indexOf("#");
						if (hashIdx !== -1) {
							var anchor = document.getElementById(url.substring(hashIdx + 1));
							if (anchor)
								anchor.scrollIntoView({behavior: "smooth"});
						} else {
							window.scrollTo(0, 0);
						}
					}
				}
				// Emit after-render
				mu._emit("mu:after-render", {url: url, finalUrl: finalUrl, mode: cfg.mode});
			};
			// Build the DOM update function
			var applyDom = function() { mu._renderPage(detail.html, cfg); };
			// Wrap in View Transition if enabled and supported.
			// startViewTransition runs the callback asynchronously (after
			// capturing the old visual state), so post-render tasks must
			// wait for the updateCallbackDone promise.
			if (cfg.transition && document.startViewTransition) {
				document.startViewTransition(applyDom).updateCallbackDone.then(postRender);
			} else {
				applyDom();
				postRender();
			}
		} catch (err) {
			if (err.name === "AbortError")
				return;
			mu._emit("mu:fetch-error", {url: url, fetchUrl: fetchUrl, error: err});
		} finally {
			if (!cfg._trigger)
				mu._hideProgress();
		}
	};

	/* ********** PAGE RENDERING (navigation mode) ********** */
	/**
	 * Parse fetched HTML and inject it into the current page.
	 * Handles source/target selection, mode application, title update,
	 * head merging, and script execution.
	 * @param	{string}	html	Raw HTML string (full page).
	 * @param	{Object}	cfg	Effective configuration.
	 */
	this._renderPage = function(html, cfg) {
		var doc = new DOMParser().parseFromString(html, "text/html");
		// Find source node in fetched document
		var sourceNode = doc.querySelector('body');
		// Find target node in current page
		var targetNode = document.querySelector('body');
		// Apply injection mode
		mu._applyMode(targetNode, sourceNode);
		// Update page title (skip when no history)
		if (cfg._addHistory)
			mu._updateTitle(doc, cfg);
		// Merge <head> elements (additive: add missing assets, never remove)
		mu._mergeHead(doc);
		// Execute scripts in injected content
		var container = document.querySelector('body');
		mu._runScripts(container);
	};

	/* ********** DOM INJECTION ********** */
	/**
	 * Replaces body contents.
	 * @param	{Element}	targetNode	Destination DOM element.
	 * @param	{Element}	sourceNode	Source DOM element to inject.
	 */
	this._applyMode = function(targetNode, sourceNode) {
		/*
		 * Deliberately using targetNode.tagName, because it is lowercase in
		 * application/xhtml+xml, where replaceWith DOES work on body and is 
		 * the modern DOM API way to do this. Just copying innerHTML in fact
		 * results in errors where there should be none.
		 */
		if (targetNode.tagName === "BODY" && sourceNode.tagName === "BODY") {
			// Cannot replace <body> with replaceWith(); in text/html use innerHTML instead
			targetNode.innerHTML = sourceNode.innerHTML;
		} else {
			targetNode.replaceWith(sourceNode);
		}
	};

	/* ********** PAGE METADATA ********** */
	/**
	 * Update the page title from the fetched document.
	 * Supports "selector/attribute" syntax: if the title selector contains
	 * a slash, the part after the slash is used as an attribute name.
	 * @param	{Document}	doc	Parsed document.
	 * @param	{Object}	cfg	Configuration.
	 */
	this._updateTitle = function(doc, cfg) {
		if (!cfg.title)
			return;
		var el = doc.querySelector(cfg.title);
		if (!el)
			return;
		var text = cfg._titleAttr ? el.getAttribute(cfg._titleAttr) : el.textContent;
		if (text)
			document.title = text;
	};
	/**
	 * Merge <head> elements from fetched document into current page.
	 * Additive only: adds missing stylesheets, styles, and scripts.
	 * Never removes existing elements.
	 * Scripts are created as new elements to ensure browser execution.
	 * @param	{Document}	doc	Parsed document.
	 */
	this._mergeHead = function(doc) {
		var selector = "link[rel='stylesheet'], style, script";
		var oldEls = document.head.querySelectorAll(selector);
		var newEls = doc.head.querySelectorAll(selector);
		// Build set of existing element keys
		var oldKeys = new Set();
		for (var i = 0; i < oldEls.length; i++)
			oldKeys.add(mu._elKey(oldEls[i]));
		// Add new elements not already present
		for (var i = 0; i < newEls.length; i++) {
			if (oldKeys.has(mu._elKey(newEls[i])))
				continue;
			if (newEls[i].tagName.toUpperCase() === "SCRIPT") {
				// Create a fresh script element (DOMParser scripts are inert)
				var s = document.createElement("script");
				for (var j = 0; j < newEls[i].attributes.length; j++)
					s.setAttribute(newEls[i].attributes[j].name, newEls[i].attributes[j].value);
				s.textContent = newEls[i].textContent;
				// Track external scripts to prevent re-loading by _runScripts
				if (s.hasAttribute("src"))
					mu._jsIncludes[s.getAttribute("src")] = true;
				document.head.appendChild(s);
			} else {
				document.head.appendChild(newEls[i].cloneNode(true));
			}
		}
	};
	/**
	 * Generate a unique key for a <head> element, used for deduplication.
	 * @param	{Element}	el	DOM element (link, style, or script).
	 * @return	{string}	Unique identifier string.
	 */
	this._elKey = function(el) {
		var tag = el.tagName.toUpperCase();
		if (tag === "LINK")
			return ("link:" + el.getAttribute("href"));
		if (tag === "STYLE")
			return ("style:" + el.textContent.substring(0, 100));
		if (tag === "SCRIPT")
			return ("script:" + (el.getAttribute("src") || el.textContent.substring(0, 100)));
		return (el.outerHTML);
	};

	/* ********** SCRIPT EXECUTION ********** */
	/**
	 * Execute <script> tags found in a container.
	 * Creates new script elements to force browser re-evaluation.
	 * External scripts (with src) are loaded only once (tracked in _jsIncludes).
	 * Scripts with mu-disabled are skipped.
	 * @param	{Element}	container	DOM element to search for scripts.
	 */
	this._runScripts = function(container) {
		var scripts = container.querySelectorAll("script");
		for (var i = 0; i < scripts.length; i++) {
			var old = scripts[i];
			// Skip already-loaded external scripts
			if (old.hasAttribute("src")) {
				var src = old.getAttribute("src");
				if (mu._jsIncludes[src])
					continue;
				mu._jsIncludes[src] = true;
			}
			// Create a new script element (forces browser re-evaluation)
			var s = document.createElement("script");
			for (var j = 0; j < old.attributes.length; j++)
				s.setAttribute(old.attributes[j].name, old.attributes[j].value);
			s.textContent = old.textContent;
			old.parentNode.replaceChild(s, old);
		}
	};

	/* ********** PROGRESS BAR ********** */
	/**
	 * Show the progress bar at the top of the page.
	 * Creates the element on first call. The bar animates to 70% width
	 * while the request is in progress.
	 */
	this._showProgress = function() {
		if (!mu._cfg.progress)
			return;
		if (!mu._progressEl) {
			mu._progressEl = document.createElement("div");
			mu._progressEl.id = "mu-progress";
			mu._progressEl.style.cssText = "position:fixed;top:0;left:0;height:3px;background:#29d;z-index:99999;transition:width .3s ease;width:0";
		}
		document.body.appendChild(mu._progressEl);
		mu._progressEl.offsetWidth; // force reflow
		mu._progressEl.style.width = "70%";
	};
	/**
	 * Hide the progress bar.
	 * Animates to 100% width, then resets and removes the element.
	 */
	this._hideProgress = function() {
		if (!mu._progressEl)
			return;
		mu._progressEl.style.width = "100%";
		setTimeout(function() {
			if (!mu._progressEl)
				return;
			mu._progressEl.style.transition = "none";
			mu._progressEl.style.width = "0";
			mu._progressEl.offsetWidth; // force reflow
			mu._progressEl.style.transition = "width .3s ease";
			mu._progressEl.remove();
		}, 200);
	};

	/* ********** SCROLL MANAGEMENT ********** */
	/**
	 * Save the current scroll position into the current history state.
	 * Called before each navigation so that popstate can restore it.
	 */
	this._saveScroll = function() {
		var state = window.history.state;
		if (state && state.mu) {
			state.scrollX = window.scrollX;
			state.scrollY = window.scrollY;
			window.history.replaceState(state, "");
		}
	};

	/* ********** EVENT EMITTER ********** */
	/**
	 * Dispatch a CustomEvent on the document.
	 * Automatically adds lastUrl and previousUrl to the detail object.
	 * @param	{string}	name	Event name (e.g. "mu:before-fetch").
	 * @param	{Object}	[detail]	Event detail data.
	 * @return	{boolean}	False if the event was canceled via preventDefault().
	 */
	this._emit = function(name, detail) {
		detail = detail || {};
		detail.lastUrl = mu._lastUrl;
		detail.previousUrl = mu._previousUrl;
		var ev = new CustomEvent(name, {
			bubbles: true,
			cancelable: true,
			detail: detail
		});
		return (document.dispatchEvent(ev));
	};
};

