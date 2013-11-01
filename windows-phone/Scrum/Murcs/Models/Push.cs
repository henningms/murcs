using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Murcs.Models
{
    public class Push
    {
        public long Id { get; set; }
        public string Identifier { get; set; }

        public string Platform { get; set; }

        public string UserId { get; set; }
    }
}
