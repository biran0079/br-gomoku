var w = 100000,
    h = 4000,
    i = 0,
    root;

var tree = d3.layout.tree()
  .size([h, w]);

var diagonal = d3.svg.diagonal()
    .projection(function(d) { return [d.x, d.y]; });

d3.select("body")
    .attr("width", w)
    .attr("height", h);
var vis = d3.select("svg")
    .attr("width", w)
    .attr("height", h)
    .append("svg:g")
    .attr("transform", "translate(20, 20)");

function preProcess(tree, parent) {
  if (parent) {
    tree.parent = parent;
  }
  tree.boardData = boardToList(tree.board);
  if (tree.children) {
    for (var i = 0; i < tree.children.length; i++) {
      preProcess(tree.children[i], tree);
    }
  }
}

function renderTree(id) {
  d3.json("/trees/" + id, function(json) {
    preProcess(json);
    root = json;
    root.x0 = 0;
    root.y0 = 0;
    update(root);
  });
}

function boardToList(stringBoard) {
  var board = stringBoard.split('\n');
  var res = [];
  for (var i = 0; i < 15; i++) {
    for (var j = 0; j < 15; j++) {
      var c = board[i][j];
      if (c == 'O' || c == 'X') {
        res.push([i,j,c]);
      }
    }
  }
  return res;
}

function difference(l1, l2) {
  function same(d1, d2) {
    for (var i = 0; i < 3; i++)
      if (d1[i] != d2[i]) return false;
    return true;
  }
  var res = [];
  var i = 0, j = 0;
  while (i < l1.length && j < l2.length) {
    if (same(l1[i], l2[j])) {
      i++; j++;
    } else {
      res.push(l1[i]);
      i++;
    }
  }
  while (i < l1.length) {
    res.push(l1[i++]);
  }
  return res;
}

function update(source) {
  var duration = d3.event && d3.event.altKey ? 5000 : 500;

  // Compute the new tree layout.
  var nodes = tree.nodes(root).reverse();

  // Normalize for fixed-depth.
   nodes.forEach(function(d) { d.y = d.depth * 180; });

  // Update the nodes…
  var node = vis.selectAll("g.node")
      .data(nodes, function(d) { return d.id || (d.id = ++i); });

  // Enter any new nodes at the parent's previous position.
  var nodeEnter = node.enter().append("svg:g")
      .attr("class", "node")
      .attr("transform", function(d) { return "translate(" + source.x0 + "," + source.y0 + ")"; })
      .on("click", function(d) { toggle(d); update(d); });

  nodeEnter.append("svg:circle")
      .attr('class', 'node')
      .attr("r", 1e-6)
      .style("fill", function(d) { 
        return d._children ? "lightsteelblue" : "#fff"; 
      });

  nodeEnter.append('svg:rect')
    .attr('width', '128.5px')
    .attr('height', '128.5px')
    .attr('fill', 'url(#smallGrid)');
  
  nodeEnter
    .selectAll('circle')
    .data(function(d) {return d.boardData;}, function(d) { return d[0] * 15 + d[1]; })
    .enter()
    .append('svg:circle')
    .attr('cy', function(d) {return (d[0] + 1) * 8})
    .attr('cx', function(d) {return (d[1] + 1) * 8})
    .attr('r', '3.5')
    .attr('stroke', 'black')
    .attr('stroke-width', '0.5')
    .attr('fill', function(d) {return d[2] == 'O' ? 'black' : 'white'});

  nodeEnter
    .selectAll('circle')
    .data(function(d) { return d.parent ? difference(d.boardData, d.parent.boardData) : []; },
        function(d) { return d[0] * 15 + d[1]; })
    .attr('stroke', 'red');

  // Transition nodes to their new position.
  var nodeUpdate = node.transition()
      .duration(duration)
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

  nodeUpdate.select("circle.node")
      .attr("r", 6)
      .style("fill", function(d) {
        if (d.isRefuted == 1) return 'blue';
        if (d.isGoal == 1) return 'red';
        return d._children ? "lightsteelblue" : "#fff"; 
      });

  // Transition exiting nodes to the parent's new position.
  var nodeExit = node.exit().transition()
      .duration(duration)
      .attr("transform", function(d) { return "translate(" + source.x + "," + source.y + ")"; })
      .remove();

  nodeExit.select("circle")
      .attr("r", 1e-6);

  // Update the links…
  var link = vis.selectAll("path.link")
      .data(tree.links(nodes), function(d) { return d.target.id; });

  // Enter any new links at the parent's previous position.
  link.enter().insert("svg:path", "g")
      .attr("class", "link")
      .attr("d", function(d) {
        var o = {x: source.x0, y: source.y0};
        return diagonal({source: o, target: o});
      })
    .transition()
      .duration(duration)
      .attr("d", diagonal);

  // Transition links to their new position.
  link.transition()
      .duration(duration)
      .attr("d", diagonal);

  // Transition exiting nodes to the parent's new position.
  link.exit().transition()
      .duration(duration)
      .attr("d", function(d) {
        var o = {x: source.x, y: source.y};
        return diagonal({source: o, target: o});
      })
      .remove();

  // Stash the old positions for transition.
  nodes.forEach(function(d) {
    d.x0 = d.x;
    d.y0 = d.y;
  });
}

// Toggle children.
function toggle(d) {
  if (d.children) {
    d._children = d.children;
    d.children = null;
  } else {
    d.children = d._children;
    d._children = null;
  }
}

