﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Murcs.Models
{
    public class Task
    {
        public long Id { get; set; }
        public string Text { get; set; }
        public string UserId { get; set; }

        public DateTime CreatedAt { get; set; }
    }
}
