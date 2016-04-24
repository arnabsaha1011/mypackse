#  gitabel
#  the world's smallest project management tool
#  reports relabelling times in github (time in seconds since epoch)
#  thanks to dr parnin
#  todo:
#    - ensure events sorted by time
#    - add issue id
#    - add person handle

"""
You will need to add your authorization token in the code.
Here is how you do it.
1) In terminal run the following command
curl -i -u <your_username> -d '{"scopes": ["repo", "user"], "note": "OpenSciences"}' https://api.github.com/authorizations
2) Enter ur password on prompt. You will get a JSON response. 
In that response there will be a key called "token" . 
Copy the value for that key and paste it on line marked "token" in the attached source code. 
3) Run the python file. 
     python gitable.py
"""
 
from __future__ import print_function
import urllib2
import json
import re,datetime
import sys
import pprint
 
class L():
  "Anonymous container"
  def __init__(i,**fields) : 
    i.override(fields)
  def override(i,d): i.__dict__.update(d); return i
  def __repr__(i):
    d = i.__dict__
    name = i.__class__.__name__
    return name+'{'+' '.join([':%s %s' % (k,pretty(d[k])) 
                     for k in i.show()])+ '}'
  def show(i):
    lst = [str(k)+" : "+str(v) for k,v in i.__dict__.iteritems() if v != None]
    return ',\t'.join(map(str,lst))

  
def secs(d0):
  d     = datetime.datetime(*map(int, re.split('[^\d]', d0)[:-1]))
  epoch = datetime.datetime.utcfromtimestamp(0)
  delta = d - epoch
  return delta.total_seconds()
 
def dump1(u,issues, token):
  
  request = urllib2.Request(u, headers={"Authorization" : "token "+token})
  v = urllib2.urlopen(request).read()
  w = json.loads(v)
  # pprint.pprint(w)
  if not w: return False
  for event in w:
    issue_id = event['issue']['number']
    if not event.get('label'): continue
    created_at = secs(event['created_at'])
    action = event['event']
    label_name = event['label']['name']
    user = event['actor']['login']
    milestone = event['issue']['milestone']
    if milestone != None : milestone = milestone['title']
    eventObj = L(when=created_at,
                 action = action,
                 what = label_name,
                 user = user,
                 milestone = milestone)
    all_events = issues.get(issue_id)
    if not all_events: all_events = []
    all_events.append(eventObj)
    issues[issue_id] = all_events
  return True

def dumpMilestone(u,milestones,token):
  try:
    return dumpMilestone1(u, milestones,token)
  except urllib2.HTTPError as e:
    if e.code != 404:
      print(e)
      print("404 Contact TA")
    return False
  except Exception as e:
    print(u)
    print(e)
    print("other Milestone Contact TA")
    return False
    
def dumpCommit(u,commits, token):
  try:
    return dumpCommit1(u,commits,token)
  except Exception as e: 
    print(u)
    print(e)
    print("Contact TA")
    return False

def dumpComments(u,comments, token):
  try:
    return dumpComments1(u,comments,token)
  except Exception as e: 
    print(u)
    print(e)
    print("Contact TA")
    return False

def dumpCommit1(u,commits,token):
  request = urllib2.Request(u, headers={"Authorization" : "token "+token})
  v = urllib2.urlopen(request).read()
  w = json.loads(v)
  if not w: return False
  for commit in w:
    sha = commit['sha']
    user = commit['author']['login']
    time = secs(commit['commit']['author']['date'])
    message = commit['commit']['message']
    commitObj = L(sha = sha,
                user = user,
                time = time,
                message = message)
    commits.append(commitObj)
  return True

def dumpComments1(u, comments, token):
  request = urllib2.Request(u, headers={"Authorization" : "token "+token})
  v = urllib2.urlopen(request).read()
  w = json.loads(v)
  if not w: return False
  for comment in w:
    user = comment['user']['login']
    identifier = comment['id']
    issueid = int((comment['issue_url'].split('/'))[-1])
    comment_text = comment['body']
    created_at = secs(comment['created_at'])
    updated_at = secs(comment['updated_at'])
    commentObj = L(ident = identifier,
                issue = issueid, 
                user = user,
                text = comment_text,
                created_at = created_at,
                updated_at = updated_at)
    comments.append(commentObj)
  return True


def dumpMilestone1(u, milestones, token):
  request = urllib2.Request(u, headers={"Authorization" : "token "+token})
  v = urllib2.urlopen(request).read()
  w = json.loads(v)
  # pprint.pprint(w)
  if not w or ('message' in w and w['message'] == "Not Found"): return False
  milestone = w
  identifier = milestone['id']
  milestone_id = milestone['number']
  milestone_title = milestone['title']
  milestone_description = milestone['description']
  created_at = secs(milestone['created_at'])
  due_at_string = milestone['due_on']
  due_at = secs(due_at_string) if due_at_string != None else due_at_string
  closed_at_string = milestone['closed_at']
  closed_at = secs(closed_at_string) if closed_at_string != None else closed_at_string
  user = milestone['creator']['login']
    
  milestoneObj = L(ident=identifier,
               m_id = milestone_id,
               m_title = milestone_title,
               m_description = milestone_description,
               created_at=created_at,
               due_at = due_at,
               closed_at = closed_at,
               user = user)
  print(identifier,milestone_id)
  milestones.append(milestoneObj)
  return True

def dump(u,issues, token):
  try:
    return dump1(u, issues, token)
  except Exception as e: 
    print(e)
    print("Contact TA")
    return False

def launchDump():
  token = "f6538126b4e3d30ea62f3b41538917a50eb1b2ef" # <=== arnab's token
  nameNum = 1
  nameMap = dict()

  milestoneNum = 1
  milestoneMap = dict()

  page = 1
  milestones = []
  while(True):
    url = 'https://api.github.com/repos/arnabsaha1011/mypackse/milestones?state=all'
    doNext = dumpMilestone(url, milestones, token)
    print("milestone "+ str(page))
    page += 1
    if not doNext : break
  page = 1
  # issues = dict()
  # while(True):
  #   url = 'https://api.github.com/repos/arnabsaha1011/mypackse/issues/events?page=' + str(page)
  #   doNext = dump(url, issues, token)
  #   print("issue page "+ str(page))
  #   page += 1
  #   if not doNext : break
  # page = 1
  # comments = []
  # while(True):
  #   url = 'https://api.github.com/repos/arnabsaha1011/mypackse/issues/comments?page='+str(page)
  #   doNext = dumpComments(url, comments, token)
  #   print("comments page "+ str(page))
  #   page += 1
  #   if not doNext : break
  # page = 1
  # commits = []
  # while(True):
  #   url = 'https://api.github.com/repos/arnabsaha1011/mypackse/commits?page=' + str(page)
  #   doNext = dumpCommit(url, commits, token)
  #   print("commit page "+ str(page))
  #   page += 1
  #   if not doNext : break
  issueTuples = []
  eventTuples = []
  milestoneTuples = []
  commentTuples = []
  commitTuples = []


  for milestone in milestones:
    if not milestone.user in nameMap:
      nameMap[milestone.user] = str(nameNum)
      nameNum+=1
    milestoneMap[milestone.m_title] = milestone.m_id
    milestoneTuples.append([milestone.m_id, milestone.m_title, milestone.m_description, milestone.created_at, milestone.due_at, milestone.closed_at, nameMap[milestone.user], milestone.ident])
    print(milestone.m_id, milestone.m_title, milestone.m_description, milestone.created_at, milestone.due_at)

  # for issue, issueObj in issues.iteritems():
  #   events = issueObj[1]
  #   issueTuples.append([issue, issueObj[0]]);
  #   #print("ISSUE " + str(issue) + ", " + issueObj[0])
  #   for event in events:
  #     #print(event.show())
  #     if not event.user in nameMap:
  #       nameMap[event.user] = str(nameNum)
  #       nameNum+=1
  #     if event.action == 'assigned' and not event.what in nameMap:
  #       nameMap[event.what] = str(nameNum)
  #       nameNum+=1
  #     eventTuples.append([issue, event.when, event.action, nameMap[event.what] if event.action == 'assigned' else event.what, nameMap[event.user], event.milestone if 'milestone' in event.__dict__ else None, event.ident])
  #   print(issue, event.when, event.action)

  # for comment in comments:
  #   if not comment.user in nameMap:
  #     nameMap[comment.user] = str(nameNum)
  #     nameNum+=1
  #   commentTuples.append([comment.issue, nameMap[comment.user], comment.created_at, comment.updated_at, comment.text, comment.ident])

  # for commit in commits:
  #   if not commit.user in nameMap:
  #     nameMap[commit.user] = str(nameNum)
  #     nameNum+=1
  #   commitTuples.append([commit.time, commit.sha, nameMap[commit.user], commit.message])

launchDump()